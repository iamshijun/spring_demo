package cjava.walker.common.adivce;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemArchitecture {

    /**
     * A join point is in the web layer if the method is defined
     * in a type in the cjava.walker.common.web package or any sub-package
     * under that.
     */
    @Pointcut("within(cjava.walker.web..*)")
    public void inWebLayer() {}

    /**
     * A join point is in the service layer if the method is defined
     * in a type in the cjava.walker.common.service package or any sub-package
     * under that.
     */
    @Pointcut("within(cjava.walker.common.service..*)")
    public void inServiceLayer() {}

    /**
     * A join point is in the data access layer if the method is defined
     * in a type in the cjava.walker.common.dao package or any sub-package
     * under that.
     */
    @Pointcut("within(cjava.walker.common.repository..*)")
    public void inDataAccessLayer() {}

    /**
     * A business service is the execution of any method defined on a service
     * interface. This definition assumes that interfaces are placed in the
     * "service" package, and that implementation types are in sub-packages.
     *
     * If you group service interfaces by functional area (for example,
     * in packages cjava.walker.common.abc.service and cjava.walker.common.def.service) then
     * the pointcut expression "execution(* cjava.walker.common..service.*.*(..))"
     * could be used instead.
     *
     * Alternatively, you can write the expression using the 'bean'
     * PCD, like so "bean(*Service)". (This assumes that you have
     * named your Spring service beans in a consistent fashion.)
     */
    @Pointcut("execution(* cjava.walker.common..service.*.*(..))")
    public void businessService() {}

    /**
     * A data access operation is the execution of any method defined on a
     * dao interface. This definition assumes that interfaces are placed in the
     * "dao" package, and that implementation types are in sub-packages.
     */
    @Pointcut("execution(* cjava.walker.common.dao.*.*(..))")
    public void dataAccessOperation() {}

}