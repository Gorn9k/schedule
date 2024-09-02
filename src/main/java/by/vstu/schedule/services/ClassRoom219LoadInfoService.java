package by.vstu.schedule.services;

import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClassRoom219LoadInfoService {

    ClassRoom219LoadInfo getClassRoom219LoadInfoById(Integer classRoom219LoadInfoId);

    List<ClassRoom219LoadInfo> getClassRoom219LoadsInfoByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    ClassRoom219LoadInfo saveClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo);

    ClassRoom219LoadInfo updateClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo);

    void deleteClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo);
}
