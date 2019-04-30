import React, { Component } from 'react';
import { requireNativeComponent, Platform, View } from 'react-native';

const RNTShadowView = requireNativeComponent('RNTShadowView', ShadowView);

class ShadowView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
      if (Platform.OS === 'ios') {
        return (<View
          {...this.props}
        />)
      }
      const { style } = this.props || {};
      let flattenedStyle = {};
      if (Array.isArray(style)) {
        style.map((item) => {
          Object.keys(item).map(key => flattenedStyle[key] = item[key]);
        })
      }
      else {
        flattenedStyle = style || {};
      }

      delete flattenedStyle.elevation;
      
      const {
        shadowColor,
        shadowOffset,
        shadowOpacity,
        shadowRadius,
        borderRadius,
        backgroundColor,
        borderWidth,
        borderColor,
      } = flattenedStyle;
      const {width, height} = shadowOffset || {}

      return (
        <RNTShadowView
              {...this.props}
              style={flattenedStyle}
              borderWidth={borderWidth}
              borderColor={borderColor !== undefined ? borderColor : 'black'}
              backgroundColor={backgroundColor}
              borderRadius={borderRadius}
              shadowColor={shadowColor !== undefined ? shadowColor : 'black'}
              shadowOffsetX={width}
              shadowOffsetY={height}
              shadowOpacity={(shadowOpacity !== undefined ? shadowOpacity : 0)}
              shadowRadius={(shadowRadius !== undefined ? shadowRadius : 2.8)}
            >
              {this.props.children}
            </RNTShadowView>
    );
    }
}

export default ShadowView