//
// Created by w2204 on 2023/9/5.
//

#ifndef MY_APPLICATION_CALLBACK_H
#define MY_APPLICATION_CALLBACK_H

namespace SambaClient {

template<typename... Ts>
class Callback {
public:
  virtual int operator()(Ts... args) const = 0;
};

}

#endif //MY_APPLICATION_CALLBACK_H
