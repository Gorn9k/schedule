package by.vstu.schedule.services;

import by.vstu.schedule.models.entities.ClassRoom219LoadInfo;
import by.vstu.schedule.repositories.ClassRoom219LoadInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseClassRoom219LoadInfoService implements ClassRoom219LoadInfoService {

    private final ClassRoom219LoadInfoRepository classRoom219LoadInfoRepository;

    @Override
    public ClassRoom219LoadInfo getClassRoom219LoadInfoById(Integer classRoom219LoadInfoId) {
        return classRoom219LoadInfoRepository.findById(classRoom219LoadInfoId).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<ClassRoom219LoadInfo> getClassRoom219LoadsInfoByStartDateAndEndDate(LocalDate startDate, LocalDate endDate) {
        List<ClassRoom219LoadInfo> classRoom219LoadInfoList = classRoom219LoadInfoRepository
                .findClassRoom219LoadsInfoByLocalDateAfterAndLocalDateBefore(startDate, endDate);
        classRoom219LoadInfoList.sort(Comparator.comparing(ClassRoom219LoadInfo::getLocalDate)
                .thenComparing(ClassRoom219LoadInfo::getLocalTime));
        return classRoom219LoadInfoList;
    }

    @Override
    public ClassRoom219LoadInfo saveClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo) {
        return classRoom219LoadInfoRepository.save(classRoom219LoadInfo);
    }

    @Override
    public ClassRoom219LoadInfo updateClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo) {
        ClassRoom219LoadInfo entity = classRoom219LoadInfoRepository.findById(classRoom219LoadInfo.getId()).orElseThrow(RuntimeException::new);
        entity.setLocalDate(classRoom219LoadInfo.getLocalDate());
        entity.setLocalTime(classRoom219LoadInfo.getLocalTime());
        entity.setType(classRoom219LoadInfo.getType());
        entity.setResponsible(classRoom219LoadInfo.getResponsible());
        entity.setDescription(classRoom219LoadInfo.getDescription());
        return classRoom219LoadInfoRepository.save(entity);
    }

    @Override
    public void deleteClassRoom219LoadInfo(ClassRoom219LoadInfo classRoom219LoadInfo) {
        classRoom219LoadInfoRepository.delete(classRoom219LoadInfo);
    }
}
