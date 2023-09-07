//
// Created by w2204 on 2023/9/4.
//

#ifndef MY_APPLICATION_CREDENTIALCACHE_H
#define MY_APPLICATION_CREDENTIALCACHE_H

#include <unordered_map>
#include <string>

namespace SambaClient {

struct CredentialTuple {
    std::string workgroup;
    std::string username;
    std::string password;
};

class CredentialCache {
 public:
  struct CredentialTuple get(const std::string &key) const;
  void put(const char *key, const struct CredentialTuple &tuple);
  void remove(const char *key);
 private:
  std::unordered_map<std::string, CredentialTuple> credentialMap_;
};

}

#endif //MY_APPLICATION_CREDENTIALCACHE_H
