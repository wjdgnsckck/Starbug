import React, {useState} from "react";
import ProductList from "./ProductList";
import ProductCategory from "./ProductCategory";

export default function Product() {

    const [activeTab, setAc] = useState(1);

    const handleTabClick = (tabNumber) => {
        setAc(tabNumber);
    };

    return (<>
        <div className="bodyContainer">
            <div >
                {/*<h3> 상품관리 페이지 입니다. </h3>*/}

                <div className={"tabbuttonbody"}>
                    <div className="tabbuttons">
                        <button onClick={() => handleTabClick(1)} className={activeTab === 1 ? 'tabbutton selectProductCategory' : 'tabbutton'}>카테고리</button>
                        <button onClick={() => handleTabClick(2)} className={activeTab === 2 ? 'tabbutton selectProductCategory' : 'tabbutton'}>상품</button>
                    </div>
                    <div className="tabContent">
                        {activeTab === 1 && <ProductCategory />}
                        {activeTab === 2 && <ProductList />}
                    </div>
                </div>

            </div>
        </div>
    </>)
}