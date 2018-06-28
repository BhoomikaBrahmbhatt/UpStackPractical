package com.giphy.upstackpractical.model;



import java.util.List;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;





        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class GiphyBean {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
    public class Datum {


        public String getUpVote() {
            return upVote;
        }

        public void setUpVote(String upVote) {
            this.upVote = upVote;
        }

        public String getDownVote() {
            return downVote;
        }

        public void setDownVote(String downVote) {
            this.downVote = downVote;
        }

        String upVote;
        String downVote;

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("content_url")
        @Expose
        private String contentUrl;
        @SerializedName("images")
        @Expose
        private Images images;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("_score")
        @Expose
        private Double score;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

    }
    public class FixedHeightSmallStill {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

    }
    public class Images {

        @SerializedName("fixed_height_small_still")
        @Expose
        private FixedHeightSmallStill fixedHeightSmallStill;
        @SerializedName("looping")
        @Expose
        private Looping looping;
        @SerializedName("original_mp4")
        @Expose
        private OriginalMp4 originalMp4;

        public FixedHeightSmallStill getFixedHeightSmallStill() {
            return fixedHeightSmallStill;
        }

        public void setFixedHeightSmallStill(FixedHeightSmallStill fixedHeightSmallStill) {
            this.fixedHeightSmallStill = fixedHeightSmallStill;
        }

        public Looping getLooping() {
            return looping;
        }

        public void setLooping(Looping looping) {
            this.looping = looping;
        }

        public OriginalMp4 getOriginalMp4() {
            return originalMp4;
        }

        public void setOriginalMp4(OriginalMp4 originalMp4) {
            this.originalMp4 = originalMp4;
        }

    }
    public class Looping {

        @SerializedName("mp4")
        @Expose
        private String mp4;
        @SerializedName("mp4_size")
        @Expose
        private String mp4Size;

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getMp4Size() {
            return mp4Size;
        }

        public void setMp4Size(String mp4Size) {
            this.mp4Size = mp4Size;
        }

    }
    public class Meta {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("response_id")
        @Expose
        private String responseId;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getResponseId() {
            return responseId;
        }

        public void setResponseId(String responseId) {
            this.responseId = responseId;
        }

    }
    public class OriginalMp4 {

        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("mp4")
        @Expose
        private String mp4;
        @SerializedName("mp4_size")
        @Expose
        private String mp4Size;

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getMp4() {
            return mp4;
        }

        public void setMp4(String mp4) {
            this.mp4 = mp4;
        }

        public String getMp4Size() {
            return mp4Size;
        }

        public void setMp4Size(String mp4Size) {
            this.mp4Size = mp4Size;
        }

    }
    public class Pagination {

        @SerializedName("total_count")
        @Expose
        private Integer totalCount;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("offset")
        @Expose
        private Integer offset;

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

    }
}
