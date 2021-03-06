package com.uic.cs581.model;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ResourcePool {

    // entire resource pool
    private static final List<Resource> entirePool = new ArrayList<>();
    private static ListIterator<Resource> entirePoolIterator;

    // current resource pool based on the simulation time
    private static final List<Resource> currentPool = new ArrayList<>();

    // completed resource pool - either reached destination or expired
    private static final List<Resource> assignedPool = new ArrayList<>();

    private static final List<Resource> expiredPool = new ArrayList<>();

    public static List<Resource> getEntirePool() {
        return entirePool;
    }

    public static List<Resource> getCurrentPool() {
        return currentPool;
    }

    public static List<Resource> getExpiredPool() {
        return expiredPool;
    }

    public static List<Resource> getAssignedPool() {
        return assignedPool;
    }


    /**
     * Current pool resources is updated based on MLT left and cab assignment.
     * Fetch new resources from entire pool based on Simulation current time.
     *
     * @return boolean: true if entire pool has resources left, else false.
     */
    public static boolean updateCurrentPool() {

        //iterate over current pool, remove expired resources to completed pool.
        //later use this iterator to add element form entire pool
        Iterator<Resource> currentPoolItr = currentPool.listIterator();

        //TODO iterate over assinged pool, if the resource has been dropped, put in cab pool

        while (currentPoolItr.hasNext()) {
            Resource temp = currentPoolItr.next();

            // expiration time(mlt) is exhausted then remove and add to assigned pool
            // or a resource is assigned a cab
            // update expired time left attribute
            if (temp.getCabId() > 0 || temp.getExpirationTimeLeftInMillis() <= 0) {
                // expired pool should be different
                currentPoolItr.remove();
                if (temp.getCabId() > 0) {
                    assignedPool.add(temp);
                } else {
                    expiredPool.add(temp);
                }
            }
        }

        //iterate over entire pool from last breakpoint , remove them and move to current pool.
        entirePoolIterator = Optional.ofNullable(entirePoolIterator).orElse(entirePool.listIterator());

        while (entirePoolIterator.hasNext()) {
            Resource temp = entirePoolIterator.next();
            // add to current pool if Request time within simulation start and current time
            if (temp.getRequestTimeInMillis() >= SimulationClock.getSimStartTime() &&
                    temp.getRequestTimeInMillis() <= SimulationClock.getSimCurrentTime()) {
                entirePoolIterator.remove();
                currentPool.add(temp);

            } else {
                log.debug("Resource ReqTime is:" + new Date(temp.getRequestTimeInMillis()) +
                        "and Sim curr time is:" +
                        new Date(SimulationClock.getSimCurrentTime()));
                entirePoolIterator.previous();
                break;
            }

        }

        //sort current pool based on MLT left - required in ascending order
        currentPool.sort((cab1, cab2) -> cab2.getExpirationTimeLeftInMillis().compareTo(cab1.getExpirationTimeLeftInMillis()));

        return entirePoolIterator.hasNext() && currentPool.size() > 0;

    }

}
