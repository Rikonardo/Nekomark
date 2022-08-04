package main

//#cgo CFLAGS: -I{{include}}
//#cgo CFLAGS: -I{{include_os}}
//#include <jni.h>
//
//__attribute__((weak))
//jclass GetObjectClass(JNIEnv* env, jobject obj) {
//	return (*env)->GetObjectClass (env, obj);
//}
//__attribute__((weak))
//jmethodID GetMethodID(JNIEnv* env, jclass clazz, char* name, char* sig) {
//	return (*env)->GetMethodID (env, clazz, name, sig);
//}
//__attribute__((weak))
//jlong CallLongMethod(JNIEnv* env, jobject obj, jmethodID methodID) {
//	return (*env)->CallLongMethod (env, obj, methodID);
//}
import "C"

//export Java_com_rikonardo_nekomark_benchmark_nativetest_NativeTest_test
func Java_com_rikonardo_nekomark_benchmark_nativetest_NativeTest_test(env *C.JNIEnv, obj C.jobject) C.jlong {
	class := C.GetObjectClass(env, obj)
	method := C.GetMethodID(env, class, C.CString("nanos"), C.CString("()J"))
	return C.CallLongMethod(env, obj, method)
}

func main() {}
