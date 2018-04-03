package com.wyq.taskSeven.service;

import com.wyq.taskSeven.pojo.Student;

public interface BaseService {
    public Long addStudent(Student student);

    public Integer updateStudent(Student student);

    public Student selectByPhone(Long phone);

    public Student selectByStudentId(Long sutdentId);

}
