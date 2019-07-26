module.exports = function(context) {

    var fs = require('fs'),
        path = require('path');

    var platformRoot = path.join(context.opts.projectRoot, 'platforms/android/app/src/main/');


    var manifestFile = path.join(platformRoot, 'AndroidManifest.xml');

    if (fs.existsSync(manifestFile)) {

        fs.readFile(manifestFile, 'utf8', function (err,data) {
            if (err) {
                throw new Error('没有找到 AndroidManifest.xml: ' + err);
            }

            var appClass = 'cn.hhjjj.mobpush.MobPushApplication';

            if (data.indexOf(appClass) == -1) {

                var result = data.replace(/<application/g, '<application android:name="' + appClass + '"');

                fs.writeFile(manifestFile, result, 'utf8', function (err) {
                    if (err) throw new Error('不能把android:name="cn.hhjjj.mobpush.MobPushApplication"写入AndroidManifest.xml: ' + err);
                })
            }
        });
    }


};