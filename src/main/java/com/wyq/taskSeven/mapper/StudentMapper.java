package com.wyq.taskSeven.mapper;

import com.wyq.taskSeven.pojo.Student;
import java.util.List;

public interface StudentMapper {
    int deleteByPrimaryKey(Long studentId);

    Long insert(Student record);

    Student selectByPrimaryKey(Long studentId);

    List<Student> selectAll();

    int updateByPrimaryKey(Student student);

    long selectPrimaryKeyByPhone(Long studentPhone);

    long selectPrimaryKeyByEmail(String studentEmail);
}