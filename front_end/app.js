// app.js
App({
  startInter : function(){
    setInterval(
        function () {
            // TODO 你需要无限循环执行的任务
            wx.login({
              success: (res) => {
                /*wx.setStorage({
                  key: 'code',
                  data: res.code
                })*/
                //发post请求 获取token并存入本地
                wx.request({
                  url: 'https://api.revocat.tech/user/login',
                  method:'POST',
                  data:{
                    code:res.code
                  },
                  success:(res)=>{
                    if(res.data.success==0){
                      console.log(res.data.msg);
                    }else{wx.setStorage({
                      key:'token',
                      data:res.data.token
                    })};
                  }
                })
                //进行一次用户信息的拉取
              },
            });
        }, 300000);    
  },
  myLogin(){
    wx.login({
      success: (res) => {
        //拉取的所有信息一律存储到本地
        /*wx.setStorage({
          key: 'code',
          data: res.code
        })*/
        //console.log(res.code);
        //发post请求 获取token并存入本地
        wx.request({
          url: 'https://api.revocat.tech/user/login',
          method:'POST',
          data:{
            code:res.code
          },
          success:(res)=>{
            if(res.data.success==0){
              console.log(res.data.msg);
            }else{
              wx.setStorage({
              key:'token',
              data:res.data.token
            })};
          }
        })
        //进行一次用户信息的拉取
      },
    });
  },
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    //进行第一次
    //this.myLogin();
    //this.startInter();
    wx.getStorage({
      key:'userInfo',
      success:(res)=>{
        //不是第一次授权了
        wx.setStorage({
          key:'isUp',
          data:'false'
        })
      }
    })
    wx.setStorageSync('logs', logs)
    wx.setStorage({
      key:'isUp',
      data:'true'
    })
    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
  },
  globalData: {
    userInfo: null
  }
})
