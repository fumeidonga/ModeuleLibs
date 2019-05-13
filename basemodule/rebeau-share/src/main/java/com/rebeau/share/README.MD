<per>
        int i = 9;
        ModuleShareEntity.Builder builder = new ModuleShareEntity.Builder();
        ModuleShareEntity entity = builder.type("local_image")
                .title("title " + i)
                .qrcodeUrl("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html")
                .sharePath(i)
                .invite_code("djelgjqqq")
                .localimage(R.drawable.navbar_icon_back_gray)
                .imageurl("http://mobile.umeng.com/images/pic/home/social/img-1.png")
                .thumbimage("")
                .description("test " + i)
                .shareurl("http://mobile.umeng.com/social")
                .build();
        //ModuleShareManager.getSingleton().postShare(mActivity, 0, entity, null);
        ModuleShareManager.getSingleton().setShareResourceId(R.drawable.share_img_cdk);
        ModuleShareManager.getSingleton().showHorizontalDialog(mActivity, entity, new ModuleShareManager.IShareCallBack() {
            @Override
            public void onStart(String shareType) {
                LogCat.d();
            }

            @Override
            public void onResult(String shareType) {
                LogCat.d();

            }

            @Override
            public void onError(String shareType, Throwable throwable) {
                LogCat.d();

            }

            @Override
            public void onCancel(String shareType) {
                LogCat.d();

            }

            @Override
            public void shareOther(ModuleShareEntity shareEntity) {
                LogCat.d();

            }
        });
</per>