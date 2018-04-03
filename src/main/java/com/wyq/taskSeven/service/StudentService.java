package com.wyq.taskSeven.service;

import com.wyq.taskSeven.mapper.StudentMapper;
import com.wyq.taskSeven.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements BaseService {

    @Autowired
    private StudentMapper studentMapper;

    public Long addStudent(Student student) {
        return studentMapper.insert(student);
    }

    public Integer updateStudent(Student student) {
        return studentMapper.updateByPrimaryKey(student);
    }

    public Student selectByPhone(Long phone) {
        long studentId = studentMapper.selectPrimaryKeyByPhone(phone);
        return studentMapper.selectByPrimaryKey(studentId);
    }

    public Student selectByStudentId(Long studentId) {
        return studentMapper.selectByPrimaryKey(studentId);
    }
}
