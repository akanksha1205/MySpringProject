package com.infy.infyinterns.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.infy.infyinterns.exception.InfyInternException;

@Aspect
@Component
public class LoggingAspect
{
	private Logger log;

    private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.infyinterns.service.*Impl.*(..))", throwing = "exception")
    public void logServiceException(InfyInternException exception)
    {
	// code
    	log.error(exception.getMessage(), exception);
    }

}
