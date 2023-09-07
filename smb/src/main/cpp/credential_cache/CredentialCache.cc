//
// Created by w2204 on 2023/9/4.
//

#include "CredentialCache.h"

namespace SambaClient {

struct CredentialTuple emptyTuple_;

struct CredentialTuple CredentialCache::get(const std::string &key) const {
    if (credentialMap_.find(key) != credentialMap_.end()) {
        return credentialMap_.at(key);
    } else {
        return emptyTuple_;
    }
}

void CredentialCache::put(const char *key, const struct CredentialTuple &tuple) {
  credentialMap_[key] = tuple;
}

void CredentialCache::remove(const char *key_) {
    std::string  key(key_);
    credentialMap_.erase(key);
}
}