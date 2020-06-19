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
        Collection<String> optionalAttendees = request.getOptionalAttendees();

        // Edge Cases
        if (duration >= TimeRange.WHOLE_DAY.duration()) {
            return avaliableTimes;
        }
        else if ((attendees.isEmpty() && optionalAttendees.isEmpty()) || events.isEmpty()){
            avaliableTimes.add(TimeRange.WHOLE_DAY);
            return avaliableTimes;
        }

        // Get Collection of unavaliableTimes times
        Collection<TimeRange> unavaliableTimes = getUnavaliableTimes(events, attendees);
        // Sort Unavaliable times by start time
        List<TimeRange> unavaliableTimesList = unavaliableTimes.stream().sorted(TimeRange.ORDER_BY_START).collect(Collectors.toList());
        // Create Avaliable times
        avaliableTimes = generateAvaliableTimes(unavaliableTimesList, duration);

        // If no conflicting times for attendees, check optional attendees
        if (avaliableTimes.equals(TimeRange.WHOLE_DAY)){
            unavaliableTimes = getUnavaliableTimes(events, optionalAttendees);
            unavaliableTimesList = unavaliableTimes.stream().sorted(TimeRange.ORDER_BY_START).collect(Collectors.toList());
            avaliableTimes = generateAvaliableTimes(unavaliableTimesList, duration);
        }
        return avaliableTimes;
    }

    public Collection<TimeRange> getUnavaliableTimes(Collection<Event> events, Collection<String> attendees) {
        Collection<TimeRange> badTimes = new ArrayList<>();
        // Used to ensure only one attendee has to attend another event to make the time unavaliable
        boolean isAnAttendeeAtThisEvent;
        for (Event event : events){
            Set<String> eventAttendees = event.getAttendees();
            isAnAttendeeAtThisEvent = false;
            for (String attendee : attendees){
                if (eventAttendees.contains(attendee) && !isAnAttendeeAtThisEvent){
                    badTimes.add(event.getWhen());
                    isAnAttendeeAtThisEvent = true;
                }
            }
        }
        return badTimes;
    }

    public Collection<TimeRange> generateAvaliableTimes(List<TimeRange> unavaliableTimes, long duration) {
        Collection<TimeRange> avaliableTimesList = new ArrayList<>();
        TimeRange avaliableTimeRange = TimeRange.WHOLE_DAY;
        for (TimeRange unavaliableTimeRange : unavaliableTimes){
            if (unavaliableTimeRange.overlaps(avaliableTimeRange)) {
                // Create new Time for av Times
                TimeRange newTime = TimeRange.fromStartEnd(avaliableTimeRange.start(), unavaliableTimeRange.start(), /* inclusive of end= */ false); 
                if (compareDuration(newTime, duration)){
                    // Add new time to avaliableTimesList
                    avaliableTimesList.add(newTime);
                }
                avaliableTimeRange = TimeRange.fromStartEnd(unavaliableTimeRange.end(), TimeRange.END_OF_DAY, /* inclusive of end= */ true);
            }
        }
        // Adding rest of day if there's time left
        if (avaliableTimeRange.duration() > 0){
        avaliableTimesList.add(avaliableTimeRange);
        }
        return avaliableTimesList;
    }
    
    public boolean compareDuration(TimeRange newTime, long duration) {
        // Compares the duration of the event with the proposed avaliable time.
        // If the the event is longer than the proposed time, the time is considered unavaliable.
        return (duration <= newTime.duration());
    }
}
