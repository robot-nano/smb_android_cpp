//
// Created by w2204 on 2023/9/4.
//

#include "JavaClassCache.h"
#include <string>

namespace SambaClient {
jclass JavaClassCache::get(JNIEnv *env, const char *name_) {
    std::string name(name_);
    jclass &value = cache_[name];
    if (value == NULL) {
        jclass localRef = env->FindClass(name_);
        if (localRef == NULL) {
            return NULL;
        }
        value = reinterpret_cast<jclass>(env->NewGlobalRef(localRef));
    }
    return value;
}
}