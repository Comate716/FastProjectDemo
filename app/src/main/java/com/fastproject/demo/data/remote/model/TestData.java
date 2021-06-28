package com.fastproject.demo.data.remote.model;

import java.util.List;

public class TestData {
    private String errMsg;
    private String errCode;
    private List<TestDataChildBean> data;

    public static class TestDataChildBean {
        private String testCode;
        private String testName;

        public String getTestCode() {
            return testCode;
        }

        public void setTestCode(String testCode) {
            this.testCode = testCode;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public List<TestDataChildBean> getData() {
        return data;
    }

    public void setData(List<TestDataChildBean> data) {
        this.data = data;
    }
}
