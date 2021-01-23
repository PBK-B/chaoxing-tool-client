package com.zmide.myClass.pojo;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Cloneable, Serializable {
    private int id;
    private String courseName;//课程名
    private String teacherName;//教师名
    //格式：星期-节次-单双周-房号
    private String courseTime;//上课时间
    private int startWeek;//开始周次
    private int endWeek;//结束周次

    private String classroom;//教室
    private String weekType;//单双周类型
    private int day;//星期几
    private int section;//节次


    public Course() {
    }

    public Course(String courseName, String teacherName, int startWeek, int endWeek, String courseTime) {
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.courseTime = courseTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }


    public Course clone() {
        try {
            return (Course) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", classroom='" + classroom + '\'' +
                ", weekType='" + weekType + '\'' +
                ", day=" + day +
                ", section=" + section +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", courseTime='" + courseTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return startWeek == course.startWeek &&
                endWeek == course.endWeek &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(teacherName, course.teacherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, teacherName, startWeek, endWeek);
    }

    public List<Course> toDetail() {
        List<Course> courseList = new ArrayList<>();
        if (TextUtils.isEmpty(courseTime)) return courseList;
        String[] courseArray = courseTime.split(";");
        for (int i = 0; i < courseArray.length; i++) {
            Course clone = this.clone();
            String[] info = courseArray[i].split(":");

            clone.setDay(Integer.parseInt(info[0]));
            clone.setSection(Integer.parseInt(info[1]));
            clone.setWeekType(info[2]);
            clone.setClassroom(info[3]);

            courseList.add(clone);
        }
        return courseList;
    }

    public String toTime(){
        return String.format("%d:%d:%s:%s", day, section, weekType, classroom);
    }

    public static Course toCourse(List<Course> courseList, int id){
        if(null == courseList)return null;
        Course course = new Course();
        course.setId(id);
        StringBuffer sb = new StringBuffer();
        for(int i = 0, len = courseList.size(); i < len; i++){
            sb.append(courseList.get(i).toTime());
            if(i != len - 1)sb.append(";");
        }
        course.setCourseTime(String.valueOf(sb));
        return course;
    }
}
