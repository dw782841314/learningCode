$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'JW/jwloggingevent/list',
        datatype: "json",
        colModel: [
            {label: '日志ID', name: 'logId', index: 'LOG_ID', width: 50, key: true},
            {label: '用户ID', name: 'userId', index: 'USER_ID', width: 80},
            {label: '调用接口', name: 'operation', index: 'OPERATION', width: 80},
            {label: '入参', name: 'params', index: 'PARAMS', width: 80},
            {label: '出参', name: 'returnParams', index: 'RETURN_PARAMS', width: 80},
            {label: '访问时间', name: 'createDate', index: 'CREATE_DATE', width: 80},
            {label: '证书ID', name: 'digitId', index: 'DIGIT_ID', width: 80},
            {
                label: '传入的是否为证书', name: 'isDigit', index: 'IS_DIGIT', width: 80, formatter:function(value, options, row) {
                    if (value == '1') {
                        return '是';
                    } else {
                        return '否';
                    }
                }
            }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        jwLoggingEvent: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.jwLoggingEvent = {};
        },
        update: function (event) {
            var logId = getSelectedRow();
            if (logId == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(logId)
        },
        saveOrUpdate: function (event) {
            var url = vm.jwLoggingEvent.logId == null ? "JW/jwloggingevent/save" : "JW/jwloggingevent/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.jwLoggingEvent),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var logIds = getSelectedRows();
            if (logIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "JW/jwloggingevent/delete",
                    contentType: "application/json",
                    data: JSON.stringify(logIds),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (logId) {
            $.get(baseURL + "JW/jwloggingevent/info/" + logId, function (r) {
                vm.jwLoggingEvent = r.jwLoggingEvent;
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'userId': vm.jwLoggingEvent.userId,
                    'startYm': vm.jwLoggingEvent.startYm,
                    'endYm': vm.jwLoggingEvent.endYm
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});