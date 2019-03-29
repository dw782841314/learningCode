$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'JW/jwdigitcert/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'digitId', index: 'DIGIT_ID', width: 50, key: true},
			{ label: '证书标识', name: 'digitCertNo', index: 'DIGIT_CERT_NO', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', index: 'CREATE_TIME', width: 80 }, 			
			{ label: '最后调用时间', name: 'updateTime', index: 'UPDATE_TIME', width: 80 },
            {
                label: '操作', name: 'endTime', width: 80, sortable: false, formatter: function (value, options, row) {
                   return '<a class="btn-enclosure" href="data:image/jpeg;base64,'+row.digitCert+'" target="_blank" data-id="' + row.digitCert + '">' + '查看证书' + '</a>';
                }
            }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		jwDigitCert: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.jwDigitCert = {};
		},
		update: function (event) {
			var digitId = getSelectedRow();
			if(digitId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(digitId)
		},
		saveOrUpdate: function (event) {
			var url = vm.jwDigitCert.digitId == null ? "JW/jwdigitcert/save" : "JW/jwdigitcert/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.jwDigitCert),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var digitIds = getSelectedRows();
			console.log(digitIds)
			if(digitIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "JW/jwdigitcert/delete",
                    contentType: "application/json",
				    data: JSON.stringify(digitIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(digitId){
			$.get(baseURL + "JW/jwdigitcert/info/"+digitId, function(r){
                vm.jwDigitCert = r.jwDigitCert;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData: {
                    'digitId': vm.jwDigitCert.digitId,
                    'digitCertNo': vm.jwDigitCert.digitCertNo
                },
                page:page
            }).trigger("reloadGrid");
		}
	}
});