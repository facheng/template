package com.template.framework.manager.factory;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.template.common.constant.Constants;
import com.template.common.utils.AddressUtils;
import com.template.common.utils.LogUtils;
import com.template.common.utils.ServletUtils;
import com.template.common.utils.security.ShiroUtils;
import com.template.common.utils.spring.SpringUtils;
import com.template.project.monitor.logininfor.domain.Logininfor;
import com.template.project.monitor.logininfor.service.LogininforServiceImpl;
import com.template.project.monitor.online.domain.OnlineSession;
import com.template.project.monitor.online.domain.UserOnline;
import com.template.project.monitor.online.service.IUserOnlineService;
import com.template.project.monitor.operlog.domain.OperLog;
import com.template.project.monitor.operlog.service.IOperLogService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 异步工厂（产生任务用）
 * 
 */
public class AsyncFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFactory.class);

    /**
     * 同步session到数据库
     * 
     * @param session
     *            在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(final OnlineSession session) {
        return new TimerTask() {
            @Override
            public void run() {
                UserOnline online = new UserOnline();
                online.setSessionId(String.valueOf(session.getId()));
                online.setDeptName(session.getDeptName());
                online.setLoginName(session.getLoginName());
                online.setStartTimestamp(session.getStartTimestamp());
                online.setLastAccessTime(session.getLastAccessTime());
                online.setExpireTime(session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(session.getStatus());
                online.setSession(session);
                LOGGER.info("start syncSessionToDb for user name {}", session.getLoginName());

                IUserOnlineService userOnlineService = SpringUtils.getBean(IUserOnlineService.class);
                if (userOnlineService.selectOnlineById(online.getSessionId()) != null) {
                    userOnlineService.updateUserOnline(online);
                } else {
                    userOnlineService.saveOnline(online);
                }

            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog
     *            操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final OperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(IOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 记录登陆信息
     * 
     * @param username
     *            用户名
     * @param status
     *            状态
     * @param message
     *            消息
     * @param args
     *            列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = ShiroUtils.getIp();
        return new TimerTask() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(AddressUtils.getRealAddressByIP(ip));
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                Logininfor logininfor = new Logininfor();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(LogininforServiceImpl.class).insertLogininfor(logininfor);
            }
        };
    }
}
