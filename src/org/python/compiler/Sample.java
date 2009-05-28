package org.python.compiler;
import java.dyn.*;
import org.python.core.Py;
import org.python.core.PyCode;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.ThreadState;

public class Sample {
    public static void dolt() {
        ThreadState ts = Py.getThreadState();
        
        // XXX doesn't work - inserting additional arguments not yet implemented
        // PyObject result = InvokeDynamic.<PyObject>foo(ts, new PyInteger(4));

        PyObject result = InvokeDynamic.<PyObject>foo(ts, (PyObject)new PyInteger(4), null, null, null);

        System.out.println("result = " + result);
    }

    static { Linkage.registerBootstrapMethod("bootstrapDynamic"); }
    private static CallSite bootstrapDynamic(Class<?> caller, String name, MethodType type) {
        CallSite site = new CallSite(caller, name, type);

        ThreadState ts = Py.getThreadState();
        PyCode func_code = ((PyFunction)ts.frame.getname(name)).func_code;

        MethodType oneArgCall = MethodType.fromMethodDescriptorString(
                "(Lorg/python/core/ThreadState;" + // state
                "Lorg.python.core.PyObject;" + // arg1
                "Lorg/python/core/PyObject;" + // globals
                "[Lorg/python/core/PyObject;" + // defaults
                "Lorg/python/core/PyObject;)" + // closure
                "Lorg/python/core/PyObject;", null);
        System.err.println("method type: " + oneArgCall);
        System.err.println("call site type: " + type);
        MethodHandle call = MethodHandles.lookup().findVirtual(PyCode.class, "call", oneArgCall);
        System.err.println("untransformed method handle: " + call);
        call = MethodHandles.convertArguments(call, type);
        call = MethodHandles.insertArgument(call, 0, func_code);
        // call = MethodHandles.insertArgument(call, 2, null); // globals - XXX doesn't work, not yet implemented
        // call = MethodHandles.insertArgument(call, 3, null); // defaults
        // call = MethodHandles.insertArgument(call, 4, null); // closure
        System.err.println("transformed method handle: " + call);
        site.setTarget(call);
        return site;
    }
}
