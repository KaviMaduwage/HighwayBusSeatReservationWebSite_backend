package com.project.seatReservation.service;

import com.project.seatReservation.dao.ScheduleDao;
import com.project.seatReservation.dao.TripCrewDao;
import com.project.seatReservation.model.Schedule;
import com.project.seatReservation.model.TripCrew;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    ScheduleDao scheduleDao;
    TripCrewDao tripCrewDao;

    public ScheduleServiceImpl(ScheduleDao scheduleDao, TripCrewDao tripCrewDao) {
        this.scheduleDao = scheduleDao;
        this.tripCrewDao = tripCrewDao;
    }

    @Override
    public List<Schedule> findScheduleByOriginDestinationAndStartDateTime(Date tripDate, String origin, String destination, String startTime) {
        return scheduleDao.findScheduleByOriginDestinationAndStartDateTime(tripDate,origin,destination,startTime);
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleDao.save(schedule);

    }

    @Override
    public List<TripCrew> getCrewListByScheduleId(int scheduleId) {
        return tripCrewDao.getCrewListByScheduleId(scheduleId);
    }

    @Override
    public void saveTripCrew(List<TripCrew> toBeSavedCrewList) {
        tripCrewDao.saveAll(toBeSavedCrewList);
    }

    @Override
    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        return scheduleDao.save(schedule);
    }

    @Override
    @Transactional
    public void updateTripCrew(TripCrew tripCrew) {
        tripCrewDao.save(tripCrew);
    }

    @Override
    public List<Schedule> findScheduleByDate(Date date) {
        return scheduleDao.findScheduleByDate(date);
    }

    @Override
    public List<Schedule> findScheduleById(int scheduleId) {
        return scheduleDao.findScheduleById(scheduleId);
    }

    @Override
    public List<Schedule> findBusScheduleByDateTownAndRoute(Date date, String origin, String destination, int routeId) {
        return scheduleDao.findBusScheduleByDateTownAndRoute(date,origin,destination,routeId);
    }

    @Override
    public List<Schedule> findBusCrewTodaySchedule(String today, int busCrewId) {
        return scheduleDao.findBusCrewTodaySchedule(today,busCrewId);
    }

    @Override
    public List<Schedule> findScheduleByBusOwnerIdDate(String searchDate, int busOwnerId) {
        return scheduleDao.findScheduleByBusOwnerIdDate(searchDate,busOwnerId);
    }

    @Override
    public List<TripCrew> getConductorDetailsByScheduleId(int scheduleId) {
        return tripCrewDao.getConductorDetailsByScheduleId(scheduleId);
    }
}
