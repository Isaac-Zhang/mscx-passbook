package com.sxzhongf.mscx.passbook.log;

/**
 * LogConstants for 日志记录常量
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/29
 */
public class LogConstants {

    /**
     * 用户行为名称
     */
    public class ActionName {

        /**
         * 用户查看优惠券信息
         */
        public static final String USER_PASS_INFO = "UserPassInfo";
        /**
         * 用户查看已使用的优惠券
         */
        public static final String USER_USED_PASS_INFO = "UserUsedPassInfo";
        /**
         * 用户使用优惠券
         */
        public static final String USER_USE_PASS = "UserUsePass";
        /**
         * 用户获取优惠券库存
         */
        public static final String INVENTORY_INFO = "InventoryInfo";
        /**
         * 用户领取优惠券
         */
        public static final String DRAW_PASS_TEMPLATE = "DrawPassTemplate";
        /**
         * 创建评论
         */
        public static final String CREATE_FEEDBAK = "CreateFeedback";
        /**
         * 获取评论
         */
        public static final String GET_FEEDBAK = "GetFeedback";

    }
}
