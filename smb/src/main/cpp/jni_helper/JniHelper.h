//
// Created by w2204 on 2023/9/4.
//

#ifndef MY_APPLICATION_JNIHELPER_H
#define MY_APPLICATION_JNIHELPER_H

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_nativeInit(
        JNIEnv *env, jobject thiz, jboolean debug, jlong cache_handler);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_nativeDestroy(
        JNIEnv *env, jobject thiz, jlong handler);

JNIEXPORT jint JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_openDir(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT jobject JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_stat(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_createFile(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_mkdir(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_rmdir(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_rename(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri, jstring new_uri);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_unlink(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT jint JNICALL
Java_com_example_smb_nativefacade_NativeSambaFacade_openFile(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri, jstring mode);

JNIEXPORT jobject JNICALL
Java_com_example_smb_nativefacade_SambaDir_readDir(
    JNIEnv *env, jobject thiz, jlong handler, jint fd);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_SambaDir_close(
    JNIEnv *env, jobject thiz, jlong handler, jint fd);

JNIEXPORT jobject JNICALL
Java_com_example_smb_nativefacade_SambaFile_fstat(
    JNIEnv *env, jobject thiz, jlong handler, jint fd);

JNIEXPORT jlong JNICALL
Java_com_example_smb_nativefacade_SambaFile_seek(
    JNIEnv *env, jobject thiz, jlong handler, jint fd,
    jlong offset, jint whence);

JNIEXPORT jint JNICALL
Java_com_example_smb_nativefacade_SambaFile_read(
    JNIEnv *env, jobject thiz, jlong handler, jint fd,
    jobject buffer, jint capacity);

JNIEXPORT jint JNICALL
Java_com_example_smb_nativefacade_SambaFile_write(
    JNIEnv *env, jobject thiz, jlong handler, jint fd,
    jobject buffer, jint length);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_SambaFile_close(
    JNIEnv *env, jobject thiz, jlong handler, jint fd);

JNIEXPORT jlong JNICALL
Java_com_example_smb_nativefacade_NativeCredentialCache_nativeInit(
        JNIEnv *env, jobject thiz);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeCredentialCache_putCredential(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri,
        jstring workgroup, jstring username, jstring password);

JNIEXPORT void JNICALL
Java_com_example_smb_nativefacade_NativeCredentialCache_removeCredential(
        JNIEnv *env, jobject thiz, jlong handler, jstring uri);

JNIEXPORT void JNICALL
Java_com_example_smb_SambaConfiguration_setEnv(
    JNIEnv *env, jobject thiz, jstring var, jstring value);

#ifdef __cplusplus
}
#endif

#endif //MY_APPLICATION_JNIHELPER_H