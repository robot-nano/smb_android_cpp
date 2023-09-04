//
// Created by w2204 on 2023/9/5.
//

#ifndef MY_APPLICATION_JNICALLBACK_H
#define MY_APPLICATION_JNICALLBACK_H

#include "base/Callback.h"

#include <jni.h>
#include <functional>

template<typename T>
struct JniContext {
  JNIEnv *const env;
  const T &instance;
  JniContext(JNIEnv *const env, T &obj)
      :env(env), instance(obj) {}
};

template<typename T, typename... Us>
class JniCallback : public SambaClient::Callback<Us...> {
public:
  JniCallback(
      const JniContext<T> &context, std::function<int(JniContext<T>, Us...)> callback)
      : context(context), callback(callback) {}

  JniCallback(JniCallback &) = delete;
  JniCallback(JniCallback &&) = delete;

  int operator()(Us... args) const {
    return callback(context, args...);
  }

private:
  const JniContext<T> context;
  const std::function<int(JniContext<T>, Us...)> callback;
};

#endif //MY_APPLICATION_JNICALLBACK_H
