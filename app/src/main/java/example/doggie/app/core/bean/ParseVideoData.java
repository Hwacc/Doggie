package example.doggie.app.core.bean;

import java.util.List;

/**
 * Created by Hwa on 2017/9/29.
 */

public class ParseVideoData {
    /**
     * Status : {"Code":200,"Message":"一切安好。"}
     * Result : {"Info":{"TimeLength":214792,"Format":"hdmp4","Accept":{"Format":["flv","hdmp4","mp4"],
     * "QualityId":[3,2,1]}},"Url":{"TimeLength":214792,"FileSize":18144829,
     * "Main":"http://cn-fjxm-dx-v-02.acgvideo.com/vg5/f/07/6689771-1-hd.mp4?expires=1506662700&platform=pc&ssig=ElvsZfS52Y36WdmOrIQvUg&oi=1867067035&nfa=iKQJtLw3Fy5f05Q/mDvFYw==&dynamic=1&hfa=2080283850&hfb=Yjk5ZmZjM2M1YzY4ZjAwYTMzMTIzYmIyNWY4ODJkNWI="
     * ,"Backup":["http://tx.acgvideo.com/3/d8/6689771-1-hd.mp4?txTime=1506662893&platform=pc&txSecret=ff280eb463c4be85cfd54f8268caaa3a&oi=1867067035&rate=1000&hfb=b99ffc3c5c68f00a33123bb25f882d5b","http://ws.acgvideo.com/b/d5/6689771-1hd.mp4?wsTime=1506662893&platform=pc&wsSecret2=40513b9a6eff29e11b41fe7b9f2972f7&oi=1867067035&rate=1"]}}
     */

    private StatusBean Status;
    private ResultBean Result;

    public StatusBean getStatus(){
        return Status;
    }

    public ResultBean getResult(){
        return Result;
    }

    public static class StatusBean {
        /**
         * Code : 200
         * Message : 一切安好。
         */

        private int Code;
        private String Message;

        public int getCode() {
            return Code;
        }

        public void setCode(int Code) {
            this.Code = Code;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }
    }

    public static class ResultBean {
        /**
         * Info : {"TimeLength":214792,"Format":"hdmp4","Accept":{"Format":["flv","hdmp4","mp4"],"QualityId":[3,2,1]}}
         * Url : {"TimeLength":214792,"FileSize":18144829,"Main":"http://cn-fjxm-dx-v-02.acgvideo.com/vg5/f/07/6689771-1-hd.mp4?expires=1506662700&platform=pc&ssig=ElvsZfS52Y36WdmOrIQvUg&oi=1867067035&nfa=iKQJtLw3Fy5f05Q/mDvFYw==&dynamic=1&hfa=2080283850&hfb=Yjk5ZmZjM2M1YzY4ZjAwYTMzMTIzYmIyNWY4ODJkNWI=","Backup":["http://tx.acgvideo.com/3/d8/6689771-1-hd.mp4?txTime=1506662893&platform=pc&txSecret=ff280eb463c4be85cfd54f8268caaa3a&oi=1867067035&rate=1000&hfb=b99ffc3c5c68f00a33123bb25f882d5b","http://ws.acgvideo.com/b/d5/6689771-1hd.mp4?wsTime=1506662893&platform=pc&wsSecret2=40513b9a6eff29e11b41fe7b9f2972f7&oi=1867067035&rate=1"]}
         */

        private InfoBean Info;
        private UrlBean Url;

        public InfoBean getInfo() {
            return Info;
        }

        public void setInfo(InfoBean Info) {
            this.Info = Info;
        }

        public UrlBean getUrl() {
            return Url;
        }

        public void setUrl(UrlBean Url) {
            this.Url = Url;
        }

        public static class InfoBean {
            /**
             * TimeLength : 214792
             * Format : hdmp4
             * Accept : {"Format":["flv","hdmp4","mp4"],"QualityId":[3,2,1]}
             */

            private int TimeLength;
            private String Format;
            private AcceptBean Accept;

            public int getTimeLength() {
                return TimeLength;
            }

            public void setTimeLength(int TimeLength) {
                this.TimeLength = TimeLength;
            }

            public String getFormat() {
                return Format;
            }

            public void setFormat(String Format) {
                this.Format = Format;
            }

            public AcceptBean getAccept() {
                return Accept;
            }

            public void setAccept(AcceptBean Accept) {
                this.Accept = Accept;
            }

            public static class AcceptBean {
                private java.util.List<String> Format;
                private java.util.List<Integer> QualityId;

                public List<String> getFormat() {
                    return Format;
                }

                public void setFormat(List<String> Format) {
                    this.Format = Format;
                }

                public List<Integer> getQualityId() {
                    return QualityId;
                }

                public void setQualityId(List<Integer> QualityId) {
                    this.QualityId = QualityId;
                }
            }
        }

        public static class UrlBean {
            /**
             * TimeLength : 214792
             * FileSize : 18144829
             * Main : http://cn-fjxm-dx-v-02.acgvideo.com/vg5/f/07/6689771-1-hd.mp4?expires=1506662700&platform=pc&ssig=ElvsZfS52Y36WdmOrIQvUg&oi=1867067035&nfa=iKQJtLw3Fy5f05Q/mDvFYw==&dynamic=1&hfa=2080283850&hfb=Yjk5ZmZjM2M1YzY4ZjAwYTMzMTIzYmIyNWY4ODJkNWI=
             * Backup : ["http://tx.acgvideo.com/3/d8/6689771-1-hd.mp4?txTime=1506662893&platform=pc&txSecret=ff280eb463c4be85cfd54f8268caaa3a&oi=1867067035&rate=1000&hfb=b99ffc3c5c68f00a33123bb25f882d5b","http://ws.acgvideo.com/b/d5/6689771-1hd.mp4?wsTime=1506662893&platform=pc&wsSecret2=40513b9a6eff29e11b41fe7b9f2972f7&oi=1867067035&rate=1"]
             */

            private int TimeLength;
            private int FileSize;
            private String Main;
            private java.util.List<String> Backup;

            public int getTimeLength() {
                return TimeLength;
            }

            public void setTimeLength(int TimeLength) {
                this.TimeLength = TimeLength;
            }

            public int getFileSize() {
                return FileSize;
            }

            public void setFileSize(int FileSize) {
                this.FileSize = FileSize;
            }

            public String getMain() {
                return Main;
            }

            public void setMain(String Main) {
                this.Main = Main;
            }

            public List<String> getBackup() {
                return Backup;
            }

            public void setBackup(List<String> Backup) {
                this.Backup = Backup;
            }
        }
    }
}
