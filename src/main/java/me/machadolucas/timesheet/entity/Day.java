package me.machadolucas.timesheet.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class Day {

    private LocalDate day;
    private Duration workDuration;
    private List<LocalTime> timestamps = new LinkedList<>();
}
