# test with:
# JAVA_HOME=/path/to/openjdk/bsd-port/build/bsd-i586/j2sdk-image \
# /path/to/jython -J-Xint -J-ea -J-XX:+EnableInvokeDynamic \
# -J-XX:+EnableMethodHandles --boot -c 'import sample'

def foo(a):
    return a + 1

foo(5)

from org.python.compiler import Sample

Sample.dolt()
