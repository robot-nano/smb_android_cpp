//
// Created by w2204 on 2023/9/4.
//

#ifndef MY_APPLICATION_JAVACLASSCACHE_H
#define MY_APPLICATION_JAVACLASSCACHE_H

#include <unordered_map>
#include <map>
#include <jni.h>

namespace SambaClient {

class JavaClassCache {
public:
    jclass get(JNIEnv *env, const char *name);
private:
    std::map<std::string, jclass> cache_;
};

}

#endif //MY_APPLICATION_JAVACLASSCACHE_H
