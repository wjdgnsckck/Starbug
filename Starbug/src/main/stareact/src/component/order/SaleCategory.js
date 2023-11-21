import axios from 'axios';
import Header from '../Header.js';
import './Sale.css';

import React, { useState, useEffect } from 'react';

import SalePrint from "./SalePrint";
import BuyOutPrint from "./BuyOutPrint";
import Chart from "./SaleAndPurchaseChart";
import Income from "./Income";


// 매입 매출 페이지 전체 ================================================================
export default function SaleCategory(){

    // 선택된 카테고리
    const [ category, setCategory ] = useState(1);
    const clickCategory = (category) => {
        setCategory(category);

    }


    return(<>
        <div className="">

            {/* 카테고리 구역 */}
            <div className="saleCategoryBox">
                <div className={`saleCategory ${category ==1 ? 'selectSaleCategory' : ''}`}
                    onClick={ () => clickCategory(1)}> 매출 </div>
                <div className={`saleCategory ${category ==2 ? 'selectSaleCategory' : ''}`}
                    onClick={ () => clickCategory(2) }> 매입 </div>
                <div className={`saleCategory ${category ==3 ? 'selectSaleCategory' : ''}`}
                    onClick={ () => clickCategory(3) }> 차트 </div>
                <div className={`saleCategory ${category ==4 ? 'selectSaleCategory' : ''}`}
                    onClick={ () => clickCategory(4) }> 순이익 </div>
            </div>

            {/* 컨텐츠 구역 */}
            <div className="saleContentBox">
                {category === 1 && <SalePrint />}     {/* 매출 출력 */}
                {category === 2 && <BuyOutPrint />}   {/* 매입 출력 */}
                {category === 3 && <Chart />}         {/* 차트 출력 */}
                {category === 4 && <Income />}        {/* 순이익 */}
            </div>
        </div>

    </>)
}
