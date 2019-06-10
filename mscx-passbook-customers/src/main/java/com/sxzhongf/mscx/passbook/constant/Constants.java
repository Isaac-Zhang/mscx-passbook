package com.sxzhongf.mscx.passbook.constant;

/**
 * ConstantsEnum for 公用常量定义
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public class Constants {

    /**
     * 商户优惠券 Kafka Topic
     */
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /**
     * token 文件存储目录
     */
    public static final String TOKEN_DIR = "/temp/token/";

    /**
     * 已使用的 token 文件后缀名
     */
    public static final String USED_TOKEN_SUFFIX = "_";

    /**
     * 用户数的 redis key
     */
    public static final String USER_COUNT_REDIS_KEY = "sxzhongf-user-count";

    /**
     * User HBase Table
     */
    public class UserTable {

        /**
         * User HBase 表名
         */
        public static final String TABLE_NAME = "pb:user";

        /**
         * 基本信息列族
         */
        public static final String FAMILY_B = "b";

        /* 基本信息列族中包含的列 */
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String SEX = "sex";

        /**
         * 额外扩张信息列族
         */
        public static final String FAMILY_O = "o";

        public static final String PHONE = "phone";
        public static final String ADDRESS = "address";

    }

    /**
     * 优惠卷模版表 HBase Table
     */
    public class PassTemplateTable {

        public static final String TABLE_NAME = "pb:pass_template";
        //基本列族
        public static final String FAMILY_B = "b";
        /* 基本信息列族中包含的列 */
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String SUMMARY = "summary";
        public static final String DESC = "desc";
        public static final String HAS_TOKEN = "has_token";
        public static final String BACKGROUND = "background";

        //约束信息列族
        public static final String FAMILY_C = "c";

        public static final String LIMIT = "limit";
        public static final String START = "start";
        public static final String END = "end";
    }

    /**
     * 优惠卷表 HBase Table
     */
    public class PassTable {

        public static final String TABLE_NAME = "pb:pass";
        //基本列族
        public static final String FAMILY_I = "i";
        /* 基本信息列族中包含的列 */
        public static final String USER_ID = "user_ud";
        public static final String TEMPLATE_ID = "template_id";
        public static final String TOKEN = "token";
        public static final String ASSIGNED_DATE = "assigned_date";
        // consumer_date 消费时间
        public static final String CON_DATE = "con_date";

    }

    /**
     * 反馈表 HBase Table
     */
    public class FeedbackTable {

        public static final String TABLE_NAME = "pb:feedback";
        //基本列族
        public static final String FAMILY_I = "i";
        /* 基本信息列族中包含的列 */
        public static final String USER_ID = "user_ud";
        // 评论类型
        public static final String TYPE = "type";
        //pass_template RowKey ,如果是APP评论，则为-1
        public static final String TEMPLATE_ID = "template_id";
        public static final String COMMENT = "comment";

    }

}
