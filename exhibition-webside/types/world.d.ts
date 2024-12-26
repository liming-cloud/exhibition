declare module '*/world.json' {
  interface Feature {
    type: 'Feature'
    properties: {
      name: string
      [key: string]: any
    }
    geometry: {
      type: 'Polygon' | 'MultiPolygon'
      coordinates: number[][][] | number[][][][]
    }
  }

  interface GeoJSON {
    type: 'FeatureCollection'
    features: Feature[]
  }

  const value: GeoJSON
  export default value
} 