package deltix.qsrv.comm.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.LocatableAnnotation;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

/** 
 * Patched version of JAXB's RuntimeAnnotationReader that marks java.lang.Exception as @XmlTransient
 * 
 * @see <a href="http://wiki.jboss.org/wiki/Wiki.jsp?page=JAXBIntroductions">JAXB Introductions</a>
 * 
 * @author Andy Malakov
 */
public class QSJaxbAnnotationReader extends
		AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method>
		implements RuntimeAnnotationReader {

	private final RuntimeAnnotationReader delegate = new RuntimeInlineAnnotationReader();

	private static class XmlTransientProxyHandler implements InvocationHandler  {

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			throw new UnsupportedOperationException ("@XmlTransient doesn't support any properties: " + method.getName());
		}
		
		public static XmlTransient create () {
		    return (XmlTransient) Proxy.newProxyInstance(XmlTransientProxyHandler.class.getClassLoader(),
	    	        new Class[]{XmlTransient.class},
	    	        new XmlTransientProxyHandler());
		}
	}
	
	public boolean hasClassAnnotation(Class clazz, Class<? extends Annotation> annotationType) {
		if (clazz.equals(StackTraceElement.class) &&
			XmlTransient.class.isAssignableFrom(annotationType)) {
			return true;
		}
		return delegate.hasClassAnnotation(clazz, annotationType);
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getClassAnnotation(Class<A> annotationType, Class clazz, Locatable srcPos) {
		if (clazz.equals(StackTraceElement.class) && 
			XmlTransient.class.isAssignableFrom(annotationType)) {
		    return (A) XmlTransientProxyHandler.create();
		}
		return LocatableAnnotation.create(((Class<?>) clazz).getAnnotation(annotationType), srcPos);
	}

	@Override
	protected String fullName(Method m) {
		return m.getDeclaringClass().getName() + '#' + m.getName();
	}
	
	
	public <A extends Annotation> A getFieldAnnotation(Class<A> annotationType, Field field, Locatable srcPos) {
		return delegate.getFieldAnnotation(annotationType, field, srcPos);
	}

	public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, Field field) {
		return delegate.hasFieldAnnotation(annotationType, field);
	}

	public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
		return delegate.getAllFieldAnnotations(field, srcPos);
	}

	public boolean hasMethodAnnotation(Class<? extends Annotation> annotationType, Method method) {
// Alternative is to annotate Throwable.getStackTrace() as @XmlTransient 		
//		if (XmlTransient.class.isAssignableFrom(annotationType))
//			if (method.getDeclaringClass().equals(Throwable.class) && method.getName().equals("getStackTrace"))
//				return true;
		return delegate.hasMethodAnnotation(annotationType, method);
	}

	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType, Method method, Locatable srcPos) {
//		 Alternative is to annotate Throwable.getStackTrace() as @XmlTransient 		
//		if (XmlTransient.class.isAssignableFrom(annotationType))
//			if (method.getDeclaringClass().equals(Throwable.class) && method.getName().equals("getStackTrace")) {
//			    return (A) XmlTransientProxyHandler.create();
//			}

		return delegate.getMethodAnnotation(annotationType, method, srcPos);
	}


	public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
		return delegate.getAllMethodAnnotations(method, srcPos);
	}

	public <A extends Annotation> A getMethodParameterAnnotation(Class<A> annotation, Method method, int paramIndex, Locatable srcPos) {
		return delegate.getMethodParameterAnnotation(annotation, method, paramIndex, srcPos);
	}

	public <A extends Annotation> A getPackageAnnotation(Class<A> a, Class clazz, Locatable srcPos) {
		return delegate.getPackageAnnotation(a, clazz, srcPos);
	}

	public Class getClassValue(Annotation a, String name) {
		return (Class) delegate.getClassValue(a, name);
	}

	public Class[] getClassArrayValue(Annotation a, String name) {
		return (Class[]) delegate.getClassArrayValue(a, name);
	}


}
