package org.jigang.plat.file.csv;

/**
 * csv文件处理结果
 * <p>
 * Created by wujigang on 16/8/29.
 */
public class CsvDealResult {
    private Boolean success; //是否成功
    private Integer successNum; //执行成功的数量
    private Integer failedNum; //执行失败的数量
    private Long costTime; //耗时,单位毫秒
    private String remark;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailedNum() {
        return failedNum;
    }

    public void setFailedNum(Integer failedNum) {
        this.failedNum = failedNum;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
