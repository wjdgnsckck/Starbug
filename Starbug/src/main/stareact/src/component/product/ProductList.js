
import axios from 'axios';
import Header from '../Header.js';
import './Product.css';
import './ProductRegist.css'
import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import Soldout from "../img/soldout.png";
import ExcelImg from "../img/엑셀_로고.png";
import excelLogo from "../img/엑셀_로고.png";

export default function ProductList(){

    let [ productCategorys, setProductCategorys] = useState([]); // 상품 카테고리
    let [ selectedCategory, setSelectedCategory] = useState(-1); // 선택한 카테고리
    let [ productList, setProductList ] = useState([
        {
            pno:0,
            pname:"", // 상품 이름
            pprice:0, // 상품 가격
            pcno:0, // 상품 카테고리 식별번호
            pcname:"", // 상품 카테고리 이름
            resnos:[], // 재료들
            pimg:"", // 상품 이미지 경로
            pevent:0 // 이벤트 여부
        }
    ]); // 선택한 카테고리의 상퓸 리스트
    let [ keyword, setKeyword ] = useState(""); // 검색 기능
    let [ updateProduct, setUpdateProduct ] = useState({ // 업데이트 할 상품
        pno : 0,
        pname : ' ',
        pprice : 0
    });
    let [ soldoutProduct, setSoldoutProduct ] = useState([]); // 품절된 상품 리스트
    const [searchSelect, setSearchSelect] = useState("pno"); // 검색기준

    // 상품 카테고리 가져오기
    useEffect(() => {

        axios.get('/product/category/get').then(r => {
            setProductCategorys(r.data);
        })

    }, []);

    // 상품 삭제
    const onDelete = (pno) => {
        console.log('onDelete')
        let askDelete = window.confirm("정말 삭제하시겠습니까?");
        if(askDelete) {
            console.log(pno);
            axios.delete('/product/delete', {params: {pno:pno}}).then(r => {
                if(r.data) {
                    window.location.href="/product?headerCategory=4";
                    alert('삭제가 완료되었습니다.');
                }else{
                    alert('삭제에 실패하였습니다.');
                }
            })

        }
        else alert('삭제가 취소되었습니다.')
    }

    // 선택 카테고리 변경
    const changeCategory = (changePcno) => {
        console.log(changePcno);
        setSelectedCategory(changePcno);
        setKeyword("");
    }

    // 제품 불러오기
    useEffect(() => {
        console.log(selectedCategory);
        if(selectedCategory == 0) return;

        fetchData();
    }, [selectedCategory, updateProduct, searchSelect]);

    // 제품 불러오기 비동기
    async function fetchData() {
        console.log("fetchData");
        console.log(selectedCategory)
        let searchSelect = document.querySelector(".productSearchSelect").value;
        console.log(searchSelect);
        const response = await axios.get('/product/get', { params: { pcno: selectedCategory, keyword: keyword, searchSelect : searchSelect } });
        if (response.data !== null) {
            console.log("불러온 상품 정보들 ▼");
            console.log(response.data);
            setProductList(response.data);
        }else{

        }
    }

    // 제품 품절 리스트 가져오기
    useEffect(() => {
        if(productList.length > 0) {
            console.log(productList);
            axios.post('/product/get/soldout', productList).then(r => {
                setSoldoutProduct(r.data);
            });
        }
    }, [productList]);





/*  // 제품 품절 리스트 가져오기
    useEffect((e) => {
        console.log(productList);
        axios.post('/product/get/soldout', productList).then(r => {
            setSoldoutProduct(r.data);
        })
    },[productList])*/

    // 상품 수정
   const onUpdate = () => {
        console.log('onUpdate')

       async function updateAsync(){
           try{
               const response = await axios.put('/product/put',   updateProduct )
               console.log(response);
               if (response.data !== null) {
                   alert('수정이 완료되었습니다.')
                   setUpdateProduct({...updateProduct, pno:0, pname:'', pprice:0})
               }
           } catch(error){
               console.error(error);
           }
       }
       updateAsync();

    }



    // 검색 기능
    const onSearch = () => {
        console.log("onSearch");
        if(keyword=="") {
            alert("키워드를 넣어주세요."); return;
        }

        //setSelectedCategory(0);

        async function searchAsync(){
            try{
                const response = await axios.get('/product/get', { params: { pcno: 0, keyword: keyword, searchSelect : "pno" } })
                console.log(response);
                if (response.data !== null) {
                    setProductList(response.data)

                    if(response.data.length === 0) alert("검색 결과가 없습니다.")
                    console.log(productList);
                    setSelectedCategory(0);
                    console.log(selectedCategory);
                }else{
                    alert()
                }
            } catch(error){
                console.error(error);
            }
        }
        searchAsync();

    }

    // 이벤트 메뉴 설정
    const onChangeEventMenu = (pno, pevent) => {
       let isEvent = false;

       if(pevent===0) {
           pevent = window.prompt("할인할 가격을 입력해주세요")
           isEvent = true;
       }
        console.log(pno)
        console.log(pevent)
       axios.put("/product/event?pno=" + pno + "&pevent=" + pevent + "&isEvent=" + isEvent ).then(r => {
           if(r.data) {
               alert('설정 완료');
               fetchData();
           }
       })
    }

    // 검색기준 select 바꼈을 때
    const searchSelectChange = () => {
       console.log("test");
        fetchData();
    }


    // -------------------------------- ProductRegist ---------------------------------

    let [ resourceCount, setresourceCount] = useState([]); // 재료 선택 갯수

    // 재료 선택 갯수 +1
    const onAdd = ()=>{
        resourceCount.push( resourceCount.length );
        setresourceCount( [...resourceCount] );
    }

    // 상품 등록
    const registerProduct = () => {
        let resnos = []
        let resno = document.querySelectorAll(".resnoSelect"); // 재료들
        let rquantity = document.querySelectorAll(".rquantity"); // 필요수량들
        for(let i = 0; i < resno.length; i++) {
            resnos.push({"resno":resno[i].value,"rquantity":rquantity[i].value})
        }

        let info = {
            "pname": document.querySelector(".pname").value,
            "pprice": document.querySelector(".pprice").value,
            "pcno":document.querySelector(".pcnoSelect").value,
            "resnos":resnos
        }

        /*
                {
                  "pname" : "딸기딸기스무디",
                  "pprice" :"3900",
                  "pcno" : "5",
                  "resnos" : [
                        {"rquantity" : 0.02, "resno" : 2},
                        {"rquantity" : 0.02, "resno" : 4}
                    ]
                }

         */

        axios.post('/product/post', info).then(r => {
            alert('등록 완료 되었습니다.!');
            window.location.href="/product?headerCategory=4";
        })
    }



        return(<>
    {  /*  예지 : bodyContainer DIV로 전체 틀의 크기 잡아놨습니다.  */ }
        <div className="bodyContainer">
            <div >
                <div className={"productTopMenu"}>
                    {/* 검색 */}
                    <div style={{marginLeft:"20px", position:"relative"}}>
                        <input type="text" value={keyword} onChange={(e)=>{setKeyword(e.target.value)}}
                               className={"productNameSearchInput"}/>
                        <button className={"registbutton"} type="button" onClick={onSearch} >검색</button>
                        <a href={"/excel/product"}>  <img src={ExcelImg} className="productExcelImg" /> </a>
                    </div>
                    
                    <div>
                        <select value={searchSelect} onChange={(e) => { setSearchSelect(e.target.value) }} className={"productSearchSelect"}>
                            <option disabled>검색기준</option>
                            <option value={"pno"}>등록순</option>
                            <option value={"pname"}>가나다순</option>
                            <option value={"pprice"}>가격순</option>
                        </select>
                    </div>
                </div>
                <div className="cateAndMenuBox">
                    <div className="menuCategory">
                    </div>
                    <div className="menuCategory">
                        <button className={`categoryBtn ${selectedCategory == -1 ? 'active' : ''}`} onClick={() => changeCategory(-1)}> 이벤트 </button>
                        {
                            productCategorys.map(category => {
                                return(<>
                                    <button className={`categoryBtn ${selectedCategory == category.pcno ? 'active' : ''}`} onClick={() => changeCategory(category.pcno)}> {category.pcname} </button>
                                </>)
                            })
                        }
                    </div>
                    <div className={"productsBox"}>
                        <div className={"products"}>
                            {

                                productList&&productList.map(product => {

                                    return(<>
                                        <div className={"productBox"}>
                                            {

                                                soldoutProduct.includes(product.pno)?
                                                    (<> <img className={"productSoldout"} src={Soldout}/> </>) : (<>  </>)
                                            }
                                            {
                                                product.pno === updateProduct.pno?
                                                    (<>
                                                        <div className={"productContent"}>
                                                            <input type={"text"} className={"updateProductContent"} value={updateProduct.pname}
                                                                onChange={(e) => {setUpdateProduct({...updateProduct, pname : e.target.value})}}/>
                                                            <input type={"number"} className={"updateProductContent"} value={updateProduct.pprice}
                                                                onChange={(e) => {setUpdateProduct({...updateProduct, pprice : e.target.value})}}/>
                                                        </div>
                                                        <div className={"twoButton"}>
                                                            <button className={"buttons"} type={"button"} onClick={(e) => setUpdateProduct({...updateProduct, pno:0, pname:'', pprice:0})}>수정취소</button>
                                                            <button className={"buttons"} type={"button"} onClick={onUpdate}>수정완료</button>
                                                        </div>
                                                    </>) :
                                                product.pevent === 0 ?
                                                    (<>
                                                        <div className={"productContent"}>
                                                            <div className={"productPname"}>{product.pname}</div>
                                                            <div className={"productPprice"}>
                                                                {product.pprice.toLocaleString()}원
                                                            </div>
                                                        </div>
                                                        <div className={"twoButton"}>
                                                            <button className={"buttons"} type={"button"} onClick={(e)=> {
                                                                console.log('수정하기');
                                                                console.log(product);
                                                                setUpdateProduct({...updateProduct,pno:product.pno, pname:product.pname, pprice:product.pprice}, () => {
                                                                    console.log(updateProduct);
                                                                });
                                                                console.log(updateProduct);
                                                            }}>수정하기</button>
                                                            <button className={"buttons"} onClick={() => onDelete(product.pno)} type={"button"}>삭제하기</button>
                                                        </div>

                                                    </>) :
                                                    (<>

                                                        <div className={"productContent"}>
                                                            <div className={"productPname"}>{product.pname}</div>
                                                            <div className={"productPprice"}>
                                                                <span className={"originalPrice"}>{product.pprice.toLocaleString()}원</span>
                                                                {(product.pprice-product.pevent).toLocaleString()}원
                                                            </div>
                                                        </div>
                                                        <div className={"twoButton"}>
                                                            <button className={"buttons"} type={"button"} onClick={(e)=> {
                                                                console.log('수정하기');
                                                                console.log(product);
                                                                setUpdateProduct({...updateProduct,pno:product.pno, pname:product.pname, pprice:product.pprice}, () => {
                                                                    console.log(updateProduct);
                                                                });
                                                                console.log(updateProduct);
                                                            }}>수정하기</button>
                                                            <button className={"buttons"} onClick={() => onDelete(product.pno)} type={"button"}>삭제하기</button>
                                                        </div>
                                                    </>)
                                            }
                                            {
                                                product.pevent !== 0 ?
                                                    (<>
                                                        <div className={"eventButtonDiv"}><button onClick={(e) => {
                                                            onChangeEventMenu(product.pno, product.pevent)}} className={"buttons eventButton"}>이벤트 메뉴 삭제하기</button></div>
                                                    </>) :
                                                    (<>
                                                        <div className={"eventButtonDiv"}><button onClick={(e) => {
                                                            onChangeEventMenu(product.pno, product.pevent)}} className={"buttons eventButton"}>이벤트 메뉴 설정하기</button></div>
                                                    </>)
                                            }
                                        </div>
                                    </>)
                                })
                            }
                        </div>
                    </div>
                </div>
            </div>
            <div className={"productRegistBack"}>
                <div className={"productRegistBody"}>

                    <h2 className={"registProductH2"}>상품 등록</h2>

                    <div className={"registBody"}>
                        상품 이름 : <input type={"text"} className={"pname"}/> <br/>
                        상품 가격 : <input type={"number"}원 className={"pprice"}/> <br/>
                        상품 카테고리 :
                        <select className={"pcnoSelect"}>
                            {
                                productCategorys.map((productCategory)=>{
                                    return(<>
                                        <option value={productCategory.pcno}>{productCategory.pcname}</option>
                                    </>)
                                })
                            }
                            {/*
                                <option value={1}>커피</option>
                            */}
                        </select> <br/>
                        <div>
                            {
                                resourceCount.map( (r)=>{
                                    return(<><GetResoures count={r} /> </>)
                                })
                            }
                        </div>
                        <button className={"buttons"} onClick={ onAdd }>재료 추가</button> <br/>
                        <button className={"buttons"} onClick={registerProduct}>상품 등록</button>
                    </div>
                </div>
            </div>
        </div>
     </>)
}

// -------------------------------- ProductRegist ----------------------------------------------------------------
function GetResoures( props ) {

    let [ resoures, setResoures] = useState([]);

    useEffect(()=>{

        axios.get('/inventory/allresoures').then(r=>{
            console.log(r.data);
            setResoures(r.data);
            console.log(resoures);
        })
    }, [])

    return(<>
        <div className={"resouresDiv"} >
            <span> 재료 선택 : </span>
            <select className={"resnoSelect"}>
                {
                    resoures.map(res => {
                        return(<> <option value={res.resno}> {res.resname} </option> </>)
                    })
                }
                {/*<option value={1}> 원두 </option>*/}
            </select>
            <span className={"rquantitySpan"}> 용량 : </span> <input type={"number"} className={"rquantity"}/>
        </div>
    </>)
}