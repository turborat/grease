#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <jni.h>
#include "udp.h"

JNIEXPORT jlong JNICALL Java_UDPJni_udprx (JNIEnv *env, jobject self, jstring group, jint port) { 
    char* str = (char*) (*env)->GetStringUTFChars(env, group, 0); 
    struct sock* ss = udprx(str, port); 
    (*env)->ReleaseStringUTFChars(env, group, str); 
    return (jlong) ss;
}

JNIEXPORT jint JNICALL Java_UDPJni_receive (JNIEnv *env, jobject self, jlong ss, jobject buf, jint capacity) { 
  char* buf2 = (char*)(*env)->GetDirectBufferAddress(env, buf);
  return receive((struct sock*) ss, buf2, capacity); 
}

JNIEXPORT jlong JNICALL Java_UDPJni_udptx (JNIEnv *env, jobject self, jstring group, jint port) { 
    char* str = (char*) (*env)->GetStringUTFChars(env, group, 0); 
    struct sock* ss = udptx(str, port) ; 
    (*env)->ReleaseStringUTFChars(env, group, str); 
    return (jlong) ss ; 
}

JNIEXPORT jint JNICALL Java_UDPJni_sendx (JNIEnv *env, jobject self, jlong ss, jobject buf, jint len) {
  char* buf2 = (char*)(*env)->GetDirectBufferAddress(env, buf);
  return sendx((struct sock*) ss, buf2, len) ; 
}

