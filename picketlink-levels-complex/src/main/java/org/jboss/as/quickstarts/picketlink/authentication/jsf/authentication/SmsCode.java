package org.jboss.as.quickstarts.picketlink.authentication.jsf.authentication;

import org.picketlink.authentication.levels.annotations.SecurityLevel;

/**
 * <p>Class used as credential for sms code.</p>
 *
 * @author Michal Trnka
 */
@SecurityLevel("3")
public class SmsCode{
    private String code;

    public SmsCode(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
