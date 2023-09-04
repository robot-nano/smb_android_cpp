cmake_minimum_required(VERSION 3.0)
project(smblicent)

add_library(
        samba_client
        SHARED
        src/main/cpp/jni_helper/JniHelper.cc
        src/main/cpp/jni_helper/JavaClassCache.cc
        src/main/cpp/samba_client/SambaClient.cc
        src/main/cpp/credential_cache/CredentialCache.cc
)
include_directories(
        src/main/cpp
        src/main/cpp/includes
)

set(CMAKE_CXX_FLAGS
        "${CMAKE_CXX_FLAGS} -std=c++0x -O2 -D_FORTIFY_SOURCE=2 -fstack-protector-all -fPIE")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -pie")

find_library(
        log-lib
        log
)

set(libfolder "${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}")
add_library(libsmbclient
        SHARED
        IMPORTED)
set_target_properties(libsmbclient
        PROPERTIES IMPORTED_LOCATION
        ${libfolder}/libsmbclient.so)

target_link_libraries(samba_client ${log-lib} libsmbclient)