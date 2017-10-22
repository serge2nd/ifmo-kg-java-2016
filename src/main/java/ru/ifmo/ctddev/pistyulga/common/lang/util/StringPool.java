package ru.ifmo.ctddev.pistyulga.common.lang.util;

public class StringPool {
	/** Private constructor for this static class */
	private StringPool() {}
	
	public static final String
		BOOLEAN	=	boolean.class.getName(),
		CHAR	=	char.class.getName(),
		BYTE	=	byte.class.getName(),
		SHORT	=	short.class.getName(),
		INT		=	int.class.getName(),
		LONG	=	long.class.getName(),
		FLOAT	=	float.class.getName(),
		DOUBLE	=	double.class.getName(),
		VOID	=	void.class.getName(),
		NULL	=	String.valueOf((Object)null),
		
		PACKAGE =	"package",
		JAVA_LANG = "java.lang",
		NONE	=	"none";
}
