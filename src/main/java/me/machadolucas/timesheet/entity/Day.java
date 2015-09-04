package me.machadolucas.timesheet.entity;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Day {

    Date day;
    Duration workDuration;
    List<Date> timestamps;
}
