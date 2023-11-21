import './ProductCategory.css'
import React, {useEffect, useState} from "react";
import axios from "axios";

export default function ProductCategory(props) {

    let [ productCategorys, setProductCategorys] = useState([]); // 상품 카테고리
    let [ updateCategory, setUpdateCategory] = useState({
        pcno: 0,
        pcname: ""
    }); // 수정할 카테고리

    const getCategory = () => {
        axios.get('/product/category/get').then(r => {
            console.log('useEffect')
            setProductCategorys(r.data);
        })
    }

    // 상품 카테고리 가져오기
    useEffect((e) => {
        getCategory();
    }, [updateCategory]);

    const onUpdateCategory = () => { // 수정하기
        axios.put('/product/category/put', updateCategory).then(r => {
            if(r.data) {
                alert('수정 완료!');
                setUpdateCategory({...updateCategory, pcno: 0, pcname: ""})
            }else{
                alert('수정 실패..');
            }
        })
    }
    const onDeleteCategory = (pcno) => { // 삭제하기
        axios.delete('/product/category/delete', { params : {pcno : pcno}}).then(r => {
            if(r.data) {
                alert('삭제 완료!');
                getCategory();
            }else{
                alert('삭제 실패..');
            }
        })
    }
    const onRegisterCategory = () => { // 등록하기
        const info = { pcname : document.querySelector(".inputCategoryPcname").value}
        axios.post('/product/category/post', info).then(r => {
            if(r.data){
                alert('등록 완료!')
                window.location.href="/product?headerCategory=4";
            }
            else alert('등록 실패..')
        })
    }


    return (<>
        <div className={"bodyContainer"}>
            <div>
                <div className={"productCategoryBack"}>
                    <div className={"productCategoryBox"}>
                        <div className={"productCategoryManagementBox"}>
                            <h2 className={"categoryH2"}>카테고리 관리</h2>

                            <div className={"categoryBoxList"}>
                                {
                                    productCategorys.map(category => {
                                        return (<>
                                            <div className={"categoryBox"}>
                                                {
                                                    category.pcno === updateCategory.pcno?
                                                        (<>
                                                            <input type={"text"} className={"updateCategoryPcname"} value={updateCategory.pcname} onChange={
                                                                (e => {
                                                                    setUpdateCategory({...updateCategory, pcname : e.target.value})
                                                                })
                                                            } />
                                                            <div>
                                                                <button type={"button"} className={"buttons"} onClick={(e) => {
                                                                    setUpdateCategory({...updateCategory,pcno:0, pcname:""});
                                                                }}>수정취소</button>
                                                                <button type={"button"} className={"buttons"} onClick={(e) => {onUpdateCategory()}}>수정완료</button>
                                                            </div>
                                                        </>)
                                                    :
                                                        (<>
                                                        <div className={"categoryPcname"}>{category.pcname}</div>
                                                        <div>
                                                            <button type={"button"} className={"buttons"} onClick={(e) => {
                                                                console.log('수정하기');
                                                                setUpdateCategory({...updateCategory,pcno:category.pcno, pcname:category.pcname});
                                                            }}>수정하기</button>
                                                            <button type={"button"} className={"buttons"} onClick={(e) => {onDeleteCategory(category.pcno)}}>삭제하기</button>
                                                        </div>
                                                        </>)
                                                }
                                            </div>
                                        </>)
                                    })
                                }
                            </div>

                        </div>
                        <div>
                            <h2 className={"categoryH2"}>카테고리 등록</h2>

                            <div className={"registCategoryBox"}>
                                <input type={"text"} className={"inputCategoryPcname"}/>
                                <button type={"button"} className={"buttons"} onClick={(e) => {onRegisterCategory()} } >등록하기</button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>

    </>)
}