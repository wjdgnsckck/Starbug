import axios from 'axios';
import React, { useState, useEffect } from 'react';
import './Pos.css';
import Soldout from "../img/soldout.png";

import backImg from "../img/backImg.png";

// 포스 전체 ========================================================================================
export default function Pos() {
    const [productList, setProductList] = useState([]);
    const [ cart , setCart ] = useState([]);
    // 기본 카테고리를 1로 설정
    const [selectedCategory, setSelectedCategory] = useState(-1);
    const [ soldoutProduct, setSoldoutProduct ] = useState([]); // 품절된 상품 리스트

    // 상품 출력 -----------------------------------
    useEffect(() => {
        async function fetchData() {
            const response = await axios.get('/product/get', { params: { pcno: selectedCategory, keyword : "", searchSelect : "pno" } });
            if (response.data !== null) {
                console.log("불러온 상품 정보들 ▼");
                console.log(response.data);
                setProductList(response.data);
            }
        }
        fetchData();
    }, [selectedCategory]); // 카테고리가 변경될 때만 다시 불러오도록 설정

    // 카테고리 변경 -----------------------------------
    const changeCategory = (category) => {
        setSelectedCategory(category);
    }

    // 장바구니 -----------------------------------
    const listCart = (e) => {
        const selectedProduct = e.target.value;
        const product = productList.find(item => item.pname === selectedProduct);
        if (product) {
            const newItem = {
                pno : product.pno,
                pname: selectedProduct,
                pprice: product.pprice,
                pevent: product.pevent
            };
            setCart((prevCart) => [...prevCart, newItem]);
        }
    }

    // 장바구니 취소
    const onDeleteCartItem = (e) => {
        const newCart = cart.filter(item =>  e !== item )
        setCart ( newCart );
    }
    // 품절 상품 -----------------------------------
    useEffect(() => {
        if(productList.length > 0) {
            console.log(productList);
            axios.post('/product/get/soldout', productList).then(r => {
                setSoldoutProduct(r.data);
            });
        }
    }, [productList]);

    return (<>
        {/* 포스 전체 레이아웃 */}
        <div className="bodyContainer">
            { /* 로그인한 사업자 명, 현재 시간 및 날짜 표기 */}
            {/* 상품 카테고리와 상품 출력 구역*/}
            <div className="cateAndProduBox">
                {/* 카테고리 선택 버튼 */}
                <div className="posMenuCategory">
                    <button className={`poscategoryBtn ${selectedCategory ==-1 ? 'active' : ''}`} onClick={() => changeCategory(-1)}> 이벤트 </button>
                    <button className={`poscategoryBtn ${selectedCategory ==1 ? 'active' : ''}`} onClick={() => changeCategory(1)}> 커피 </button>
                    <button className={`poscategoryBtn ${selectedCategory ==2 ? 'active' : ''}`} onClick={() => changeCategory(2)}> 논 커피 </button>
                    <button className={`poscategoryBtn ${selectedCategory ==3 ? 'active' : ''}`} onClick={() => changeCategory(3)}> 에이드 </button>
                    <button className={`poscategoryBtn ${selectedCategory ==4 ? 'active' : ''}`} onClick={() => changeCategory(4)}> 주스 </button>
                    <button className={`poscategoryBtn ${selectedCategory ==5 ? 'active' : ''}`} onClick={() => changeCategory(5)}> 스무디 </button>
                </div>

                {/* 상품버튼 */}
                <div className="posProduct">
                    <CategoryPrint category={productList} listCart={listCart} soldoutProduct={soldoutProduct} />
                </div>
                <div className="goToMainBtn">
                    <a href='/main'><img src ={ backImg } alt="Image" /></a>
                 </div>
            </div>

            {/* 장바구니 출력 구역 */}
            <div className="posCartBox">
                <CartPrint cart={cart} onDeleteCartItem={onDeleteCartItem}  />
            </div>
        </div>
   </>);
}


// 상품 카테고리 태그 ========================================================================================
function CategoryPrint(props) {
    const { category, listCart } = props;
    const soldoutProduct = props.soldoutProduct

    return (<>
        <div>
            <h3>{category.pcname}</h3>
            <div>
                {category.map((product, index) => (
                    <ProductPrint key={index} product={product} listCart={listCart} soldoutProduct={soldoutProduct} />
                ))}
            </div>
        </div>
    </>);
}
// 상품 태그 ========================================================================================
function ProductPrint(props) {
    const { product, listCart } = props;
    const soldoutProduct = props.soldoutProduct
    return(<>
        {   // 품절이라면
            soldoutProduct.includes(product.pno)?
                (<>
                    <button className="posProductBtn" disabled onClick={ listCart } value={ product.pname } >
                        <img className={"posSoldout"} src={Soldout}/>
                        <span className={"salePrice"}>
                            { product.pevent > 0 ?
                                (<> -{product.pevent} </>) : (<> </>)
                            }
                        </span>
                        {product.pname}
                    </button>
                </>)
             : (<>
                    <button className="posProductBtn" onClick={ listCart } value={ product.pname } >
                        <span className={"salePrice"}>
                            { product.pevent > 0 ?
                                (<> -{product.pevent} </>) : (<> </>)
                            }
                        </span>
                        {product.pname}
                    </button>
                </>)
        }
    </>);
}
// 장바구니 태그 ========================================================================================
function CartPrint(props) {

    const [memberCoupons, setMemberCoupons] = useState([]);
    const [selectCoupons, setSelectCoupons] = useState({
        cname : '',
        cno : 0,
        ccpercent : 0
    });
    // 선택 쿠폰 변경되면 랜더링 다시 하기
    useEffect ( () => { } , [ selectCoupons ] )

    const { cart } = props;
    console.log('cart ▼');
    console.log(cart);
        console.log('selectCoupons ▼');
        console.log(selectCoupons);

        // 장바구니 총가격 넣고 빼고 할 전역 변수
        let total = 0;
        // 장바구니 내 상품들 번호 출력하기 위한 전역 변수
        let count = 1;

    // 회원의 쿠폰 정보 호출하기
    const memberCoupon = async (e) => {
        let mno = document.querySelector('.mno').value;
        console.log('mno : ' + mno);
        if( mno !== '' ){
            axios.get("/pos/getCoupon", { params: { mno: mno } })
              .then(r => {
                    console.log(r.data);
                    if (r.data) {
                      alert('회원의 쿠폰정보 호출');
                      setMemberCoupons(r.data); // 쿠폰 정보를 상태에 설정
                    } else {  alert(' 없는 회원 입니다. '); }
              });
          } else { alert(' 회원 번호를 입력하고 검색 해 주세요.')}
      }


    // axios 통신하기 : 주문 데이터 전송 -----------------------------------
    const order = (e) => {
        // 전달할 데이터 만들기
            // 1. 주문 상품들
            let cartItems = {};
            cart.forEach((item, index) => {
                cartItems[`item${index}`] = item; // 객체에 각 항목을 추가
            });

            // 2. 회원 정보 및 쿠폰 사용시 쿠폰 정보
            const selectElement = document.querySelector('select'); // select 요소 선택
            const selectedOption = selectElement.options[selectElement.selectedIndex]; // 선택된 option 요소 선택
            const chisnoValue = selectedOption.value; // 선택된 option 요소의 value 값 가져오기
            console.log('chisnoValue : ');
            console.log(chisnoValue);
            console.log(selectCoupons.cno);

            let orderMno = document.querySelector('.mno').value
            let cartMember = {
                mno : orderMno == '' ? 1 : orderMno ,
                chisno : selectCoupons.cno
            };

            // 3. 두가지 정보를 합쳐서 하나의 객체로 만들기
            let orderInfo = {
                cartItem : cartItems,
                cartMember : cartMember
            }

            console.log(orderInfo);

        // 4. 데이터 전달 후 결과 요청
        axios.post("/pos/do", orderInfo )
            .then( r => {
                console.log(r.data);
                if(r.data){
                    alert('주문이 완료 되었습니다.');
                    window.location.reload();
                }
        } );
    }


    return (<>
            <div className="cart">
                <div className="cartHeader"> CART </div>
                <ul className="cartlistin">
                    {cart.map((item, index) => (
                        <li className="cartlistLi" key={index}>
                            <div> {count++}. <span> {item.pname} </span> </div>
                            <div style={{ display: 'flex' }}> <div>{(item.pprice-item.pevent).toLocaleString()}원 </div>
                                <div className="onDeleteCartItembtn" onClick={ () => props.onDeleteCartItem(item) } > X </div>
                            </div>
                        </li>
                    ))}
                </ul>
                {/* 총 합계 */}
                <div className="cartBottom">
                    <div>
                        <div className="cartBottom02">
                            {/* 회원이 주문할 경우 회원 번호 넣기 | 비회원이면 1 넣기*/}
                            <div className="cartBottomMno">
                                회원번호 : <input className="mno" type="text" />
                                쿠폰내용 : <select className="cartBottomMselect"
                                    onChange={ (e) => {
                                       setSelectCoupons({...selectCoupons ,
                                                                    cno : parseInt(e.target.value.split("_")[0]),
                                                                    ccpercent : parseInt(e.target.value.split("_")[1])
                                                         })
                                    }}
                                >
                                    <option value="0" >   </option>
                                    {memberCoupons.map((item, index) => (
                                            <option className="chisno" key={index} value={item.chisno+"_"+item.ccpercent} >
                                              {item.cccontent}
                                            </option>
                                    ))}
                                </select>
                            </div>

                            {/* 회원 번호를 입력하고 검색을 누르면 사용 가능한 쿠폰이 출력되게 하기 */}
                            <div>
                                <button onClick={ memberCoupon } type="button" className="cartBottomSearch"> 검색 </button>
                            </div>
                        </div>
                        <div>
                            <button type="button"> 계산서 </button>
                            <button onClick={ order } type="button"> 현금결제 </button>
                            <button onClick={ order } type="button"> 카드결제 </button>
                        </div>
                    </div>
                    <div className="totalPrice">
                        {
                            selectCoupons.cno === 0
                                ? cart.reduce((sum, item) => sum + item.pprice-item.pevent, 0).toLocaleString()
                                : (
                                    (cart.reduce((sum, item) => sum + item.pprice-item.pevent, 0) * ((100-selectCoupons.ccpercent)/100))
                                ).toLocaleString()
                        }원
                    </div>
                </div>
            </div>
        </>);
}
