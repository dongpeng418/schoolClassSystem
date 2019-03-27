package cn.com.school.classinfo.model;

import lombok.Data;

import java.util.Date;

@Data
public class EvaluationBeta {
  private Integer id;
  private String evalCode;
  private String name;
  private String title;
  private Double value;
  private Date createTime;
  private String createBy;
  private Date updateTime;
  private String updateBy;
}
