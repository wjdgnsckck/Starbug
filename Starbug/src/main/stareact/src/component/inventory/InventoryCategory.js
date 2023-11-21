import InventoryLogInsert from './InventoryLogInsert.js';
import ResouresUpdate from './ResouresUpdate.js';
import Inventory from './inventory.js';
import style from './InventoryCategory.css';

import React, { useState, useEffect } from 'react';

export default function InventoryCategory(props){
    // 선택된 카테고리
     const [ inventory, setInventory ] = useState(1);
     const clickCategory = (inventory) => {
            setInventory(inventory);
}


    return(<>
        {/* 재고 페이지 상단*/}
        <div>
            <div className ="inventoryCategoryBox">
                <div className = {`inventoryCategory ${inventory ==1 ? 'selectInventoryCategory' : ''}`} onClick={()=>clickCategory(1)}>재고 현황</div>
                <div className = {`inventoryCategory ${inventory ==2 ? 'selectInventoryCategory' : ''}`} onClick={()=>clickCategory(2)}>재고 로그</div>
                <div className = {`inventoryCategory ${inventory ==3 ? 'selectInventoryCategory' : ''}`} onClick={()=>clickCategory(3)}>재고 수정</div>
            </div>
             {/* 재고 페이지 내용*/}
            <div className="inventoryContent">
                 {inventory === 1 && <Inventory />}                 {/* 재고현황 출력 */}
                 {inventory === 2 && <InventoryLogInsert />}            {/* 재고넣기 출력 */}
                 {inventory === 3 && <ResouresUpdate />}            {/* 재고수정 출력 */}

            </div>
        </div>


    </>)
}