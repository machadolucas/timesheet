package me.machadolucas.timesheet.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class Day {

    LocalDate day;
    Duration workDuration;
    List<LocalTime> timestamps;
}
