//
// Created by w2204 on 2023/9/4.
//

#ifndef MY_APPLICATION_SAMBACLIENT_H
#define MY_APPLICATION_SAMBACLIENT_H

#include "samba_include/libsmbclient.h"
#include "jni_helper/JniHelper.h"

#include <sys/types.h>
#include <vector>

namespace SambaClient {
    struct CredentialCache;

class SambaClient {
public:
    ~SambaClient();

    bool Init(const bool debug, const CredentialCache *credentialCache);

    int OpenDir(const char* uri);

    int ReadDir(const int dh, const struct smbc_dirent** dirent);

    int CloseDir(const int dh);

    int Stat(const char* url, struct stat* st);

    int Fstat(const int fd, struct stat* const st);

    int CreateFile(const char* url);

    int Mkdir(const char* url);

    int Rename(const char* url, const char* nurl);

    int Unlink(const char* url);

    int Rmdir(const char* url);

    int OpenFile(const char* url, const int flag, const mode_t mode);

    ssize_t ReadFile(const int fd, void* buffer, const size_t maxlen);

    ssize_t WriteFile(const int fd, void* buffer, const size_t length);

    off_t SeekFile(const int fd, const off_t offset, const int whence);

    int CloseFile(const int fd);

private:
    ::SMBCCTX *sambaContext = NULL;

    static void GetAuthData(const char* server,
                            const char* share,
                            char* workgroup, int maxLenWorkgroup,
                            char* username, int maxLenUsername,
                            char* password, int maxLenPassword);
};
}

#endif //MY_APPLICATION_SAMBACLIENT_H
