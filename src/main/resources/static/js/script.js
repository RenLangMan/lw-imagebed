layui.use(['upload', 'jquery', 'element', 'layer'], function () {
    const upload = layui.upload;
    const layer = layui.layer;

    $(document).on('click', '.lw-rm-file', function () {
        let that = this;
        layer.confirm('确定要删除该文件或文件夹及其下面的文件？', {
            btn: ['确认', '取消'] //按钮
        }, function (index) {
            let path = $(that).data('path');
            console.log(path);
            $.ajax({
                url: '/image/delete',
                type: 'post',
                data: {
                    'path': path,
                },
                success: res => {
                    layer.msg(res.msg, {icon: res.code === 2000 ? 6 : 5, time: 2000})
                    if (res.code === 2000) {
                        $('#lw-files-list').load(`/list?prefix=${getLastPath()}`)
                        $('#lw-path').load(`/path?prefix=${getLastPath()}`)
                    }
                }
            })
            layer.close(index)
        });
        return false;
    })

    $("#lw-mkdir").on('click', function () {
        layer.prompt({title: '请输入文件夹名称'}, function (text, index) {
            $.ajax({
                url: '/image/mkdir',
                type: 'post',
                data: {
                    'prefix': getLastPath(),
                    'dirName': text
                },
                success: res => {
                    layer.close(index)
                    layer.msg(res.msg, {icon: res.code === 2000 ? 6 : 5, time: 2000})
                    if (res.code === 2000) {
                        $('#lw-files-list').load(`/list?prefix=${getLastPath()}`)
                        $('#lw-path').load(`/path?prefix=${getLastPath()}`)
                    }
                }
            })
        });
    })

    function getdate() {
        let nowDate = new Date();
        let year = nowDate.getFullYear();
        let month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
        let day = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
        return year + month + day;
    }

    function uploadSuccess(res) {
        let imgsrc = res.msg;
        $('#img-url').val(imgsrc);
        $('#img-img').val(`<img src="${imgsrc}" alt="">`);
        $('#img-bbs').val(`[url=${imgsrc}][img]${imgsrc}[/img][/url]`);
        $('#img-mark').val(`![${imgsrc}](${imgsrc})`);
        $(".lw-image-show").html(`<img src="${imgsrc}" alt="">`);
    }

    let indexUpload;
    upload.render({
        elem: '#lw-upload',
        url: `/image/upload?prefix=${getdate()}`,
        before: function () {
            indexUpload = layer.load(0, {shade: false});
        },
        done: function (res) {
            layer.close(indexUpload)
            uploadSuccess(res)
        },
        error: function (res) {
            layer.close(indexUpload)
        }
    })
    let prefixElem = $('#prefix');
    upload.render({
        elem: '#lw-upload-pos',
        url: `/image/upload`,
        before: function () {
            let prefix = getLastPath()
            console.log("before::" + prefix);
            indexUpload = layer.load(0, {shade: false});
        },
        data: {
            prefix: function () {
                return $("#lw-path a:last-child").data("path")
            }
        },
        done: function (res) {
            let prefix = getLastPath()
            console.log(prefix);
            layer.close(indexUpload)
            uploadSuccess(res)
            $('#lw-files-list').load(`/list?prefix=${prefix}`)
            $('#lw-path').load(`/path?prefix=${prefix}`)
        },
        error: function (res) {
            layer.close(indexUpload)
        }
    })

    let clipboard = new ClipboardJS(".lw-copy", {
        text: function (param) {
            let id = $(param).data('id')
            return $(`#${id}`).val();
        }
    });

    getLastPath()

    function getLastPath() {
        return $("#lw-path a:last-child").data("path")
    }

    clipboard.on('success', function (e) {
        layer.msg('复制成功', {icon: 6})
    });

    clipboard.on('error', function (e) {
        layer.msg('复制失败', {icon: 5})
    })

    $(document).on('click', '#lw-path a', function () {
        let prefix = $(this).data('path');
        $('#lw-files-list').load(`/list?prefix=${prefix}`)
        $('#lw-path').load(`/path?prefix=${prefix}`)
    })

    $(document).on('click', '.lw-file-content', function () {
        let type = $(this).data('type');
        let path = $(this).data('path');
        let url = $(this).data('url');
        switch (type) {
            case 'dir':
                $('#lw-files-list').load(`/list?prefix=${path}`)
                $('#lw-path').load(`/path?prefix=${path}`)
                break;
            case 'image':
                $('#img-url').val(url);
                $('#img-img').val(`<img src="${url}" alt="">`);
                $('#img-bbs').val(`[url=${url}][img]${url}[/img][/url]`);
                $('#img-mark').val(`![${url}](${url})`);
                $(".lw-image-show").html(`<img src="${url}" alt="">`);
                break;
            default:
                window.open(url)
                break;
        }
    })
})
