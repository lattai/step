// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Collection<TimeRange> avaliableTimes = new ArrayList<>();
        long duration = request.getDuration();
        Collection<String> attendees = request.getAttendees();

        // Edge Cases
        if (duration >= TimeRange.WHOLE_DAY.duration()) {
            return avaliableTimes;
        }
        else if (attendees.isEmpty() || events.isEmpty()){
            avaliableTimes.add(TimeRange.WHOLE_DAY);
            return avaliableTimes;
        }

        // Get Collection of unavaliableTimes times
        Collection<TimeRange> unavaliableTimes = getUnavaliableTimes(events, attendees);
        // Sort Unavaliable times by start time
        List<TimeRange> unavTimesList = unavaliableTimes.stream().sorted(TimeRange.ORDER_BY_START).collect(Collectors.toList());
        // Create Avaliable times
        avaliableTimes = generateAvaliableTimes(unavTimesList, duration);
        return avaliableTimes;
    }

    public Collection<TimeRange> getUnavaliableTimes(Collection<Event> events, Collection<String> attendees) {
        Collection<TimeRange> badTimes = new ArrayList<>();
        int i; // Used to ensure only one attendee has to attend another event to make the time unavaliable
        for (Event event : events){
            Set<String> eventAttendees = event.getAttendees();
            i = 0;
            for (String attendee : attendees){
                if (eventAttendees.contains(attendee) && i == 0){
                    badTimes.add(event.getWhen());
                    i = 1;
                }
            }
        }
        return badTimes;
    }

    public Collection<TimeRange> generateAvaliableTimes(List<TimeRange> unavaliableTimes, long duration) {
        Collection<TimeRange> avTimes = new ArrayList<>();
        TimeRange avTime = TimeRange.WHOLE_DAY;
        for (TimeRange unAvTime : unavaliableTimes){
            if (unAvTime.overlaps(avTime)) {
                // Create new Time for av Times
                TimeRange newTime = TimeRange.fromStartEnd(avTime.start(), unAvTime.start(), false); 
                if (compareDuration(newTime, duration)){
                    // Add new time to avTimes
                    avTimes.add(newTime);
                }
                avTime = TimeRange.fromStartEnd(unAvTime.end(), TimeRange.END_OF_DAY, true);
            }
        }
        // Adding rest of day if there's time left
        if (avTime.duration() > 0){
        avTimes.add(avTime);
        }
        return avTimes;
    }
    
    public boolean compareDuration(TimeRange newTime, long duration) {
        // Compares the duration of the event with the proposed avaliable time.
        // If the the event is longer than the proposed time, the time is considered unavaliable.
        return (duration <= newTime.duration());
    }

}
