let items = [];

function addItem() {
    const length = parseFloat(document.getElementById('itemLength').value);
    const width = parseFloat(document.getElementById('itemWidth').value);
    const height = parseFloat(document.getElementById('itemHeight').value);
    const category = document.getElementById('itemCategory').value.trim();
    // 新增破損欄位
    const damaged = document.getElementById('itemDamaged') ? document.getElementById('itemDamaged').checked : false;
    if (isNaN(length) || isNaN(width) || isNaN(height) || !category) {
        alert('請完整輸入所有欄位');
        return;
    }
    items.push({ length, width, height, category, damaged });
    renderItems();
    document.getElementById('itemLength').value = '';
    document.getElementById('itemWidth').value = '';
    document.getElementById('itemHeight').value = '';
    document.getElementById('itemCategory').value = '';
    if(document.getElementById('itemDamaged')) document.getElementById('itemDamaged').checked = false;
}

function removeItem(idx) {
    items.splice(idx, 1);
    renderItems();
}

function renderItems() {
    const tbody = document.querySelector('#itemsTable tbody');
    tbody.innerHTML = '';
    items.forEach((item, idx) => {
        tbody.innerHTML += `<tr>
            <td>${idx + 1}</td>
            <td>${item.length}</td>
            <td>${item.width}</td>
            <td>${item.height}</td>
            <td>${item.category}</td>
            <td>${item.damaged ? '是' : '否'}</td>
            <td><button class="btn btn-danger btn-sm" onclick="removeItem(${idx})">移除</button></td>
        </tr>`;
    });
}

function startDispatch() {
    if (items.length === 0) {
        alert('請先加入物品');
        return;
    }
    fetch('/api/check-load', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(items)
    })
        .then(res => res.json())
        .then(data => renderResult(data))
        .catch(() => alert('派車失敗，請檢查後端服務'));
}

function renderResult(data) {
    const area = document.getElementById('resultArea');
    if (!data || (!data.assignments || data.assignments.length === 0) && (!data.unassignedItems || data.unassignedItems.length === 0)) {
        area.innerHTML = '<div class="alert alert-warning">無法派車，請檢查物品資料</div>';
        return;
    }
    let html = '';
    // 顯示已成功派車的 assignments
    if (data.assignments && data.assignments.length > 0) {
        data.assignments.forEach(assign => {
            let typeName = '';
            if (assign.vehicleType === 'GRAB_TRUCK') {
                typeName = '抓斗車';
                html += `<div class="card mb-3">
                    <div class="card-header bg-info text-white">
                        ${typeName}（${assign.vehicleNumber}號）<br>
                        已使用點數：${assign.usedPoints} / ${assign.maxPoints}
                    </div>
                    <div class="card-body">
                        <ul class="list-group">
                        ${assign.items.map(item => `<li class="list-group-item">${item.category} - ${item.length}×${item.width}×${item.height} m</li>`).join('')}
                        </ul>
                    </div>
                </div>`;
            } else if (assign.vehicleType === 'FLATBED_TRUCK') {
                typeName = '平板車';
                html += `<div class="card mb-3">
                    <div class="card-header bg-info text-white">
                        ${typeName}（${assign.vehicleNumber}號） 裝載率：
                        <div class="progress" style="height: 20px;">
                          <div class="progress-bar bg-success" role="progressbar" style="width: ${Math.round(assign.loadRate*100)}%">${Math.round(assign.loadRate*100)}%</div>
                        </div>
                    </div>
                    <div class="card-body">
                        <ul class="list-group">
                        ${assign.items.map(item => `<li class="list-group-item">${item.category} - ${item.length}×${item.width}×${item.height} m</li>`).join('')}
                        </ul>
                    </div>
                </div>`;
            } else {
                typeName = '未知車型';
                html += `<div class="card mb-3">
                    <div class="card-header bg-secondary text-white">${typeName}</div>
                </div>`;
            }
        });
    }
    // 顯示無法派車的物品清單
    if (data.unassignedItems && data.unassignedItems.length > 0) {
        html += `<div class="alert alert-danger mt-3">
            <b>無法派車清單：</b>
            <ul class="mb-0">
            ${data.unassignedItems.map(u => `<li>${u.item.category} - ${u.item.length}×${u.item.width}×${u.item.height} m，原因：${failReasonText(u.failReason)}</li>`).join('')}
            </ul>
        </div>`;
    }
    area.innerHTML = html;
}

// 失敗原因對照表
function failReasonText(reason) {
    switch(reason) {
        case 'HEIGHT_EXCEED': return '高度超過車廂';
        case 'SIZE_EXCEED': return '尺寸超過車廂';
        case 'CATEGORY_NOT_ALLOWED_OR_SIZE_EXCEED': return '類別不符或體積超過抓斗車限制';
        default: return reason;
    }
}
