//
// Created by w2204 on 2023/9/4.
//

#ifndef MY_APPLICATION_LOGGER_H
#define MY_APPLICATION_LOGGER_H

#include <android/log.h>

#define LOG(level, tag, args...) \
    __android_log_print((level), (tag), args)

#ifdef DEBUG
#define LOGV(tag, args...) LOG(ANDROID_LOG_VERBOSE, tag, args)
#define LOGD(tag, args...) LOG(ANDROID_LOG_DEBUG, tag, args)
#else // DEBUG
#define LOGV(tag, args...)
#define LOGD(tag, args...)
#endif // DEBUG
#define LOGI(tag, args...) LOG(ANDROID_LOG_INFO, tag, args)
#define LOGW(tag, args...) LOG(ANDROID_LOG_WARN, tag, args)
#define LOGE(tag, args...) LOG(ANDROID_LOG_ERROR, tag, args)
#define LOGF(tag, args...) LOG(ANDROID_LOG_FATAL, tag, args)

#endif //MY_APPLICATION_LOGGER_H
