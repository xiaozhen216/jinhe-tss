package com.jinhe.tss.framework.component.log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标记需要记录日志的业务方法。 <br/>
 * 注：记录日志的实体最好复写toString()，以规范完善日志信息。<br/><br/>
 * 
 * 记录日志时，要借助返回值 ${returnVal} 和 参数 ${args[index]} 的信息，及使用freemarker解析operateInfo
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Logable {
    
    /**
     * 操作表
     */
    String operateTable();
    
    /**
     * 操作类型，CRUD等
     */
    String operateType();
    
    /**
     * 操作信息，可带freemarker宏
     */
    String operateInfo();
    
}
